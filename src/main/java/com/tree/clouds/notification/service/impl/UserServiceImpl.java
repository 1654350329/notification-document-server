package com.tree.clouds.notification.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.notification.mapper.RegionUserMapper;
import com.tree.clouds.notification.mapper.RoleMenuMapper;
import com.tree.clouds.notification.mapper.UserMapper;
import com.tree.clouds.notification.model.bo.UserManageBO;
import com.tree.clouds.notification.model.entity.Role;
import com.tree.clouds.notification.model.entity.SysMenu;
import com.tree.clouds.notification.model.entity.User;
import com.tree.clouds.notification.model.vo.UpdatePasswordVO;
import com.tree.clouds.notification.model.vo.UserManagePageVO;
import com.tree.clouds.notification.model.vo.UserManageVO;
import com.tree.clouds.notification.service.SysMenuService;
import com.tree.clouds.notification.service.UserRoleService;
import com.tree.clouds.notification.service.UserService;
import com.tree.clouds.notification.utils.BaseBusinessException;
import com.tree.clouds.notification.utils.LoginUserUtil;
import com.tree.clouds.notification.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RegionUserMapper regionUserMapper;


    @Override
    public IPage<UserManageVO> userManagePage(UserManagePageVO userManagePageVO) {
        IPage<UserManageVO> page = userManagePageVO.getPage();
        IPage<UserManageVO> userManageVOIPage = this.baseMapper.userManagePage(page, userManagePageVO);
        List<UserManageVO> records = userManageVOIPage.getRecords();
        for (UserManageVO record : records) {
            List<Role> roleManages = userRoleService.getRoleByUserId(record.getUserId());
            List<String> roleNames = roleManages.stream().map(Role::getRoleName).collect(Collectors.toList());
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < roleNames.size(); i++) {
                if (i == roleNames.size() - 1) {
                    stringBuilder.append(roleNames.get(i));
                } else {
                    stringBuilder.append(roleNames.get(i)).append("-");
                }
            }
            record.setRoleName(stringBuilder.toString());
            record.setRoleIds(roleManages.stream().map(Role::getRoleId).collect(Collectors.toList()));
        }
        return userManageVOIPage;
    }


    @Override
    public User getUserByAccount(String account) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq(User.ACCOUNT, account);
        wrapper.eq(User.DEL, 0);
        return this.getOne(wrapper);
    }

    @Override
    public String getUserAuthorityInfo(String userId) {
        //  ROLE_admin,ROLE_normal,sys:user:list,....
        String authority = "";
        if (redisUtil.hasKey("GrantedAuthority:" + userId)) {
            authority = (String) redisUtil.get("GrantedAuthority:" + userId);
        } else {
            //获取角色
            List<Role> roles = this.userRoleService.getRoleByUserId(userId);
            authority = roles.stream().map(Role::getRoleCode).collect(Collectors.joining(","));

            List<String> menuIds = roleMenuMapper.getNavMenuIds(userId);
            List<SysMenu> menus = this.sysMenuService.listByIds(menuIds);
            String perms = menus.stream().map(SysMenu::getPerms).collect(Collectors.joining(","));
            authority = authority.concat(",").concat(perms);
            redisUtil.set("GrantedAuthority:" + userId, authority);
        }
        return authority;
    }

    @Override
    public void rebuildPassword(List<String> ids) {
        // 加密后密码
        String password = bCryptPasswordEncoder.encode("888888");

        List<User> userManages = this.listByIds(ids);
        userManages.forEach(userManage -> userManage.setPassword(password));
        this.updateBatchById(userManages);
    }

    @Override
    public void userStatus(List<String> ids, int status) {
        List<User> userManages = this.listByIds(ids);
        userManages.forEach(userManage -> userManage.setStatus(status));
        this.updateBatchById(userManages);
    }

    @Override
    public void clearUserAuthorityInfo(String userId) {
        redisUtil.del("GrantedAuthority:" + userId);
    }

    @Override
    public void addUserManage(UserManageBO userManageBO) {
        User userByAccount = this.getUserByAccount(userManageBO.getAccount());
        if (userByAccount != null) {
            throw new BaseBusinessException(400, "账号已存在,请重新输入!!");
        }
        //手机号码正则匹配
        String REGEX_MOBILE = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";
        if (!Pattern.matches(REGEX_MOBILE, userManageBO.getPhone())) {
            throw new BaseBusinessException(400, "手机号码不合法");
        }

        User userManage = BeanUtil.toBean(userManageBO, User.class);
        String password;
        if (userManageBO.getPassword() == null) {
            password = bCryptPasswordEncoder.encode("888888");
        } else {
            password = bCryptPasswordEncoder.encode(Base64.decodeStr(userManage.getPassword()));

        }
        userManage.setPassword(password);
        this.save(userManage);

        //绑定角色
        userRoleService.addRole(userManageBO.getRoleIds(), userManage.getUserId());

    }

    @Override
    public void updateUserManage(UserManageBO userManageBO) {
        User userManage = BeanUtil.toBean(userManageBO, User.class);
        this.updateById(userManage);
        //角色先删后增
        userRoleService.removeRole(userManage.getUserId());
        userRoleService.addRole(userManageBO.getRoleIds(), userManage.getUserId());
    }

    @Override
    public void deleteUserManage(String userId) {
        User userManage = new User();
        userManage.setUserId(userId);
        userManage.setDel(1);
        this.updateById(userManage);
        regionUserMapper.removeUserByUserId(userId);
        userRoleService.removeRole(userId);
    }

    @Override
    public void clearUserAuthorityInfoByMenuId(String menuId) {
        List<User> sysUsers = this.baseMapper.listByMenuId(menuId);
        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUserId());
        });
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(String roleId) {
        List<User> sysUsers = this.baseMapper.listByRoleId(roleId);
        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUserId());
        });
    }

    @Override
    public void updatePassword(UpdatePasswordVO updatePasswordVO) {
        String password = bCryptPasswordEncoder.encode(Base64.decodeStr(updatePasswordVO.getPassword()));
        User user = new User();
        user.setPassword(password);
        user.setUserId(LoginUserUtil.getUserId());
        this.updateById(user);
    }
}

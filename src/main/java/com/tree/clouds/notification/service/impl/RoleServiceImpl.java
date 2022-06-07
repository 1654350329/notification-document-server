package com.tree.clouds.notification.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.notification.mapper.RoleMapper;
import com.tree.clouds.notification.mapper.RoleMenuMapper;
import com.tree.clouds.notification.mapper.UserRoleMapper;
import com.tree.clouds.notification.model.entity.*;
import com.tree.clouds.notification.model.vo.DistributeRoleVO;
import com.tree.clouds.notification.model.vo.RoleManagePageVO;
import com.tree.clouds.notification.service.RoleService;
import com.tree.clouds.notification.service.SysMenuService;
import com.tree.clouds.notification.service.UserService;
import com.tree.clouds.notification.utils.BaseBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public List<User> getUserInfoByRole(String roleId) {
        return userRoleMapper.getUserInfoByRole(roleId);
    }

    @Override
    public IPage<Role> roleManagePage(RoleManagePageVO roleManagePageVO) {
        IPage<Role> page = roleManagePageVO.getPage();
        return this.baseMapper.roleManagePage(page, roleManagePageVO);
    }

    @Override
    @Transactional
    public void distributeRole(DistributeRoleVO distributeRoleVO) {
        this.roleMenuMapper.delete(new QueryWrapper<RoleMenu>().eq(RoleMenu.ROLE_ID, distributeRoleVO.getRoleId()));
        List<String> menuIds = distributeRoleVO.getMenuIds();
        Set<String> menuSet = new HashSet<>(menuIds);

        for (String menuId : menuIds) {
            SysMenu sysMenu = this.sysMenuService.getById(menuId);
            String pid = sysMenu.getParentId();
            while (!pid.equals("0")) {
                menuSet.add(pid);
                pid = this.sysMenuService.getById(pid).getParentId();
            }
        }
        for (String s : menuSet) {
            RoleMenu sysRoleMenu = new RoleMenu();
            sysRoleMenu.setMenuId(s);
            sysRoleMenu.setRoleId(distributeRoleVO.getRoleId());
            this.roleMenuMapper.insert(sysRoleMenu);
        }
        userService.clearUserAuthorityInfoByRoleId(distributeRoleVO.getRoleId());
    }

    @Override
    public void deleteRole(List<String> ids) {
        List<UserRole> roleUsers = userRoleMapper.selectList(new QueryWrapper<UserRole>().in(UserRole.ROLE_ID, ids));
        if (CollUtil.isNotEmpty(roleUsers)) {
            throw new BaseBusinessException(400, "角色下存在用户,不许删除!!");
        }
        this.removeByIds(ids);
    }
}

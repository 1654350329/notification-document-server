package com.tree.clouds.notification.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.notification.mapper.UserRoleMapper;
import com.tree.clouds.notification.model.entity.Role;
import com.tree.clouds.notification.model.entity.UserRole;
import com.tree.clouds.notification.service.UserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色中间表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

    @Override
    public List<Role> getRoleByUserId(String userId) {
        return this.baseMapper.getRoleByUserId(userId);
    }

    @Override
    public void addRole(List<String> roleIds, String userId) {
        for (String roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(userId);
            this.save(userRole);
        }
    }

    @Override
    public boolean removeRole(String userId) {
        QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
        wrapper.eq(UserRole.USER_ID, userId);
        return this.remove(wrapper);
    }
}

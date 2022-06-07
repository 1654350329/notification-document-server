package com.tree.clouds.notification.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.notification.model.entity.Role;
import com.tree.clouds.notification.model.entity.UserRole;

import java.util.List;

/**
 * <p>
 * 用户角色中间表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
public interface UserRoleService extends IService<UserRole> {

    List<Role> getRoleByUserId(String userId);

    void addRole(List<String> roleIds, String userId);

    boolean removeRole(String userId);
}

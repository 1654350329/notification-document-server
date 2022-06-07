package com.tree.clouds.notification.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.notification.model.entity.Role;
import com.tree.clouds.notification.model.entity.User;
import com.tree.clouds.notification.model.vo.DistributeRoleVO;
import com.tree.clouds.notification.model.vo.RoleManagePageVO;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
public interface RoleService extends IService<Role> {
    /**
     * 根据角色获取用户信息
     *
     * @param roleId
     * @return
     */
    List<User> getUserInfoByRole(String roleId);

    IPage<Role> roleManagePage(RoleManagePageVO roleManagePageVO);

    void deleteRole(List<String> ids);

    void distributeRole(DistributeRoleVO distributeRoleVO);
}

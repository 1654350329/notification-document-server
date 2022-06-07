package com.tree.clouds.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.notification.model.entity.Role;
import com.tree.clouds.notification.model.entity.User;
import com.tree.clouds.notification.model.entity.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户角色中间表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    List<Role> getRoleByUserId(@Param("userId") String userId);

    List<User> getUserInfoByRole(@Param("roleId") String roleId);
}

package com.tree.clouds.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.notification.model.entity.Role;
import com.tree.clouds.notification.model.vo.RoleManagePageVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
public interface RoleMapper extends BaseMapper<Role> {

    IPage<Role> roleManagePage(IPage<Role> page, @Param("roleManagePageVO") RoleManagePageVO roleManagePageVO);
}

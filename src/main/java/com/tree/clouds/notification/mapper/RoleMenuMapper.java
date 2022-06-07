package com.tree.clouds.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.notification.model.entity.RoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 角色菜单中间表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    List<String> getNavMenuIds(@Param("userId") String userId);
}

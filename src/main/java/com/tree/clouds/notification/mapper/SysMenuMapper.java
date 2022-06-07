package com.tree.clouds.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.notification.model.entity.SysMenu;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<String> getMenuIdByRoleId(String roleId);
}

package com.tree.clouds.notification.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.notification.model.bo.SysMenuDto;
import com.tree.clouds.notification.model.entity.SysMenu;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
public interface SysMenuService extends IService<SysMenu> {
    List<SysMenuDto> getCurrentUserNav();

    List<SysMenu> tree();

    List<String> getRole(String id);
}

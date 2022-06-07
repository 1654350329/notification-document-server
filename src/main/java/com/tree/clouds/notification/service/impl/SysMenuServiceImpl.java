package com.tree.clouds.notification.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.notification.mapper.RoleMenuMapper;
import com.tree.clouds.notification.mapper.SysMenuMapper;
import com.tree.clouds.notification.model.bo.SysMenuDto;
import com.tree.clouds.notification.model.entity.SysMenu;
import com.tree.clouds.notification.model.entity.User;
import com.tree.clouds.notification.service.SysMenuService;
import com.tree.clouds.notification.service.UserService;
import com.tree.clouds.notification.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private UserService sysUserService;

    @Autowired
    private RoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenuDto> getCurrentUserNav() {
        User sysUser = sysUserService.getById(LoginUserUtil.getUserId());

        List<String> menuIds = sysRoleMenuMapper.getNavMenuIds(sysUser.getUserId());
        List<SysMenu> menus = this.listByIds(menuIds);

        // 转树状结构
        List<SysMenu> menuTree = buildTreeMenu(menus);

        // 实体转DTO
        return convert(menuTree);
    }

    @Override
    public List<SysMenu> tree() {
        // 获取所有菜单信息
        List<SysMenu> sysMenus = this.list(new QueryWrapper<SysMenu>().orderByAsc(SysMenu.ORDER_NUM));

        // 转成树状结构
        return buildTreeMenu(sysMenus);
    }

    @Override
    public List<String> getRole(String id) {
        return this.baseMapper.getMenuIdByRoleId(id);
    }

    private List<SysMenuDto> convert(List<SysMenu> menuTree) {
        List<SysMenuDto> menuDtos = new ArrayList<>();

        menuTree.forEach(m -> {
            SysMenuDto dto = new SysMenuDto();

            dto.setId(m.getId());
            dto.setName(m.getPerms());
            dto.setTitle(m.getName());
            dto.setComponent(m.getComponent());
            dto.setPath(m.getPath());

            if (m.getChildren().size() > 0) {

                // 子节点调用当前方法进行再次转换
                dto.setChildren(convert(m.getChildren()));
            }

            menuDtos.add(dto);
        });

        return menuDtos;
    }

    private List<SysMenu> buildTreeMenu(List<SysMenu> menus) {

        List<SysMenu> finalMenus = new ArrayList<>();

        // 先各自寻找到各自的孩子
        for (SysMenu menu : menus) {

            for (SysMenu e : menus) {
                if (menu.getId().equals(e.getParentId())) {
                    menu.getChildren().add(e);
                }
            }

            // 提取出父节点
            if (menu.getParentId().equals("0")) {
                finalMenus.add(menu);
            }
        }

        System.out.println(JSONUtil.toJsonStr(finalMenus));
        return finalMenus;
    }
}

package com.tree.clouds.notification.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.notification.common.RestResponse;
import com.tree.clouds.notification.common.aop.Log;
import com.tree.clouds.notification.model.entity.Role;
import com.tree.clouds.notification.model.vo.DistributeRoleVO;
import com.tree.clouds.notification.model.vo.PublicIdsReqVO;
import com.tree.clouds.notification.model.vo.RoleManagePageVO;
import com.tree.clouds.notification.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@RestController
@RequestMapping("/role")
@Api(value = "role", tags = "角色模块")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/roleManagePage")
    @ApiOperation(value = "角色模块分页查询")
    @Log("角色模块分页查询")
    @PreAuthorize("hasAuthority('role:manage:list')")
    public RestResponse<IPage<Role>> roleManagePage(@RequestBody RoleManagePageVO roleManagePageVO) {
        IPage<Role> page = roleService.roleManagePage(roleManagePageVO);
        return RestResponse.ok(page);
    }

    @PostMapping("/addRole")
    @ApiOperation(value = "添加角色")
    @Log("添加角色")
    @PreAuthorize("hasAuthority('role:manage:add')")
    public RestResponse<Boolean> addRole(@RequestBody Role roleManage) {
        roleService.save(roleManage);
        return RestResponse.ok(true);
    }

    @PostMapping("/updateRole")
    @ApiOperation(value = "修改角色")
    @Log("修改角色")
    @PreAuthorize("hasAuthority('role:manage:update')")
    public RestResponse<Boolean> updateRole(@RequestBody Role roleManage) {
        roleService.updateById(roleManage);
        return RestResponse.ok(true);
    }

    @PostMapping("/deleteRole")
    @ApiOperation(value = "刪除角色")
    @Log("刪除角色")
    @PreAuthorize("hasAuthority('role:manage:delete')")
    public RestResponse<Boolean> deleteRole(@RequestBody PublicIdsReqVO publicIdsReqVO) {
        roleService.deleteRole(publicIdsReqVO.getIds());
        return RestResponse.ok(true);
    }

    @PostMapping("/distributeRole")
    @ApiOperation(value = "配置权限")
    @Log("配置权限")
    @PreAuthorize("hasAuthority('role:manage:distribute')")
    public RestResponse<Boolean> distributeRole(@RequestBody DistributeRoleVO distributeRoleVO) {
        roleService.distributeRole(distributeRoleVO);
        return RestResponse.ok(true);
    }
}


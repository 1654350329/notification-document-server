package com.tree.clouds.notification.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.notification.common.RestResponse;
import com.tree.clouds.notification.common.aop.Log;
import com.tree.clouds.notification.model.bo.UserManageBO;
import com.tree.clouds.notification.model.vo.PublicIdsReqVO;
import com.tree.clouds.notification.model.vo.UpdatePasswordVO;
import com.tree.clouds.notification.model.vo.UserManagePageVO;
import com.tree.clouds.notification.model.vo.UserManageVO;
import com.tree.clouds.notification.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@RestController
@RequestMapping("/user")
@Api(value = "user", tags = "用户模块")
public class UserController {
    @Autowired
    private UserService userService;

    @Log("用户模块分页查询")
    @PostMapping("/userManage")
    @ApiOperation(value = "用户模块分页查询")
    @PreAuthorize("hasAuthority('user:manage:list')")
    public RestResponse<IPage<UserManageVO>> userManage(@Validated @RequestBody UserManagePageVO userManagePageVO) {
        IPage<UserManageVO> page = userService.userManagePage(userManagePageVO);
        return RestResponse.ok(page);
    }

    @PostMapping("/addUserManage")
    @ApiOperation(value = "添加用户")
    @Log("添加用户")
    @PreAuthorize("hasAuthority('user:manage:add')")
    public RestResponse<Boolean> addUserManage(@Validated @RequestBody UserManageBO userManageBO) {
        userService.addUserManage(userManageBO);
        return RestResponse.ok(true);
    }

    @PostMapping("/updateUserManage")
    @ApiOperation(value = "修改用户")
    @Log("修改用户")
    @PreAuthorize("hasAuthority('user:manage:update')")
    public RestResponse<Boolean> updateUserManage(@Validated @RequestBody UserManageBO userManageBO) {
        userService.updateUserManage(userManageBO);
        return RestResponse.ok(true);
    }

    @PostMapping("/deleteUserManage")
    @ApiOperation(value = "刪除用户")
    @Log("刪除用户")
    @PreAuthorize("hasAuthority('user:manage:delete')")
    public RestResponse<Boolean> deleteUserManage(@RequestBody PublicIdsReqVO publicIdsReqVO) {
        for (String id : publicIdsReqVO.getIds()) {
            userService.deleteUserManage(id);
        }
        return RestResponse.ok(true);
    }

    @PostMapping("/rebuildPassword")
    @ApiOperation(value = "重置密码")
    @Log("重置密码")
    @PreAuthorize("hasAuthority('user:manage:rebuild')")
    public RestResponse<Boolean> rebuildPassword(@RequestBody PublicIdsReqVO publicIdsReqVO) {
        userService.rebuildPassword(publicIdsReqVO.getIds());
        return RestResponse.ok(true);
    }

    @PostMapping("/userStatus/{status}")
    @ApiOperation(value = "启用或停用用户 0停用 1启用")
    @Log("启用或停用用户")
    @PreAuthorize("hasAuthority('user:manage:status')")
    public RestResponse<Boolean> userStatus(@PathVariable int status, @RequestBody PublicIdsReqVO publicIdsReqVO) {
        userService.userStatus(publicIdsReqVO.getIds(), status);
        return RestResponse.ok(true);
    }

    @PostMapping("/updatePassword")
    @ApiOperation(value = "修改当前用户密码")
    @Log("修改当前用户密码")
    public RestResponse<Boolean> updatePassword(@RequestBody UpdatePasswordVO updatePasswordVO) {
        userService.updatePassword(updatePasswordVO);
        return RestResponse.ok(true);
    }
}


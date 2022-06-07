package com.tree.clouds.notification.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.notification.model.bo.UserManageBO;
import com.tree.clouds.notification.model.entity.User;
import com.tree.clouds.notification.model.vo.UpdatePasswordVO;
import com.tree.clouds.notification.model.vo.UserManagePageVO;
import com.tree.clouds.notification.model.vo.UserManageVO;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
public interface UserService extends IService<User> {

    void rebuildPassword(List<String> ids);

    void userStatus(List<String> ids, int status);

    IPage<UserManageVO> userManagePage(UserManagePageVO userManagePageVO);

    User getUserByAccount(String account);

    String getUserAuthorityInfo(String userId);

    void addUserManage(UserManageBO userManageBO);

    void updateUserManage(UserManageBO userManageBO);

    void deleteUserManage(String userId);

    void clearUserAuthorityInfo(String userId);

    void clearUserAuthorityInfoByMenuId(String menuId);

    void clearUserAuthorityInfoByRoleId(String roleId);

    void updatePassword(UpdatePasswordVO updatePasswordVO);

}

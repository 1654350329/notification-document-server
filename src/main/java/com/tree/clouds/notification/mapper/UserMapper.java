package com.tree.clouds.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.notification.model.entity.User;
import com.tree.clouds.notification.model.vo.UserManagePageVO;
import com.tree.clouds.notification.model.vo.UserManageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
public interface UserMapper extends BaseMapper<User> {

    IPage<UserManageVO> userManagePage(IPage<UserManageVO> page, @Param("userManagePageVO") UserManagePageVO userManagePageVO);

    List<User> listByMenuId(@Param("menuId") String menuId);

    List<User> listByRoleId(String roleId);
}

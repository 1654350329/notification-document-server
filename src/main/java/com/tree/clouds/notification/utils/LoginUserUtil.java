package com.tree.clouds.notification.utils;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.tree.clouds.notification.model.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author 林振坤
 * @description
 * @date 2022/1/2 0002 18:32
 */
public class LoginUserUtil {

    public static String getUserId() {
        User user = getUserManage();
        if (user == null) return null;
        return user.getUserId();

    }

    public static Integer getUserRegion() {
        User user = getUserManage();
        if (user == null) return null;
        return user.getRegionId();
    }

    public static String getUserName() {
        User user = getUserManage();
        if (user == null) return null;
        return user.getName();

    }

    private static User getUserManage() {
        try {
            if (SecurityContextHolder.getContext() == null || SecurityContextHolder.getContext().getAuthentication() == null) {
                return null;
            }
            Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
            if (ObjectUtil.isNull(credentials) || StrUtil.isBlank(credentials.toString())) {
                return null;
            }
            return (User) credentials;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }
    }

}

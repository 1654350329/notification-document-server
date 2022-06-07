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
        try {
            Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
            if (ObjectUtil.isNull(credentials) || StrUtil.isBlank(credentials.toString())) {
                return null;
            }
            User user = (User) credentials;
            return user.getUserId();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Integer getUserRegion() {
        Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (ObjectUtil.isNull(credentials) || StrUtil.isBlank(credentials.toString())) {
            return null;
        }
        User user = (User) credentials;
        return user.getRegionId();
    }

    public static String getUserName() {
        Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (ObjectUtil.isNull(credentials) || StrUtil.isBlank(credentials.toString())) {
            return null;
        }
        User user = (User) credentials;
        return user.getName();

    }

}

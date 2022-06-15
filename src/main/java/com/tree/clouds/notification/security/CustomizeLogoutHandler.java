package com.tree.clouds.notification.security;

import com.tree.clouds.notification.service.LoginLogService;
import com.tree.clouds.notification.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomizeLogoutHandler implements LogoutHandler {
    @Autowired
    private LoginLogService loginLogService;

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        loginLogService.updateLongTime(LoginUserUtil.getUserId());
    }
}

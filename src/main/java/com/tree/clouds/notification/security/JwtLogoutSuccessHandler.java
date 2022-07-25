package com.tree.clouds.notification.security;

import cn.hutool.json.JSONUtil;
import com.tree.clouds.notification.common.RestResponse;
import com.tree.clouds.notification.service.LoginLogService;
import com.tree.clouds.notification.utils.JwtUtils;
import com.tree.clouds.notification.utils.LoginUserUtil;
import com.tree.clouds.notification.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private RedisUtil redisUtil;


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        loginLogService.updateLongTime(LoginUserUtil.getUserId());
        if (LoginUserUtil.getUserId() != null) {
            redisUtil.del("GrantedAuthority:" + LoginUserUtil.getUserId());
        }
        System.out.println("LoginUserUtil.getUserId() = " + LoginUserUtil.getUserId());
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }


        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();

        response.setHeader(jwtUtils.getHeader(), "");

        RestResponse result = RestResponse.ok("");

        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));

        outputStream.flush();
        outputStream.close();
    }
}

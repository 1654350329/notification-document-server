package com.tree.clouds.notification.controller;

import cn.hutool.core.lang.UUID;
import com.google.code.kaptcha.Producer;
import com.tree.clouds.notification.common.RestResponse;
import com.tree.clouds.notification.model.entity.User;
import com.tree.clouds.notification.model.vo.PublicIdReqVO;
import com.tree.clouds.notification.service.RegionService;
import com.tree.clouds.notification.service.UserService;
import com.tree.clouds.notification.utils.LoginUserUtil;
import com.tree.clouds.notification.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@Api(value = "auth", tags = "用户登入模块")
public class AuthController {

    @Autowired
    private Producer producer;

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserService userManageService;
    @Autowired
    private RegionService regionService;

    @GetMapping("/captcha")
    @ApiOperation(value = "获取验证码")
    public RestResponse<Map> captcha() throws IOException {

        String key = UUID.randomUUID().toString();
        String code = producer.createText();
        key = "a";
        code = "123456";
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        BASE64Encoder encoder = new BASE64Encoder();
        String str = "data:image/jpeg;base64,";

        String base64Img = str + encoder.encode(outputStream.toByteArray());

        redisUtil.hset("captcha", key, code, 120);
        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("captchaImg", base64Img);
        return RestResponse.ok(map);
    }

    @GetMapping("/info")
    @ApiOperation(value = "获取用户信息")
    private RestResponse<Map> getInfo() {
        //roles, name, avatar, introduction
        User user = userManageService.getById(LoginUserUtil.getUserId());
        String userAuthorityInfo = userManageService.getUserAuthorityInfo(user.getUserId());
        List<String> roles = Arrays.stream(userAuthorityInfo.split(",")).collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        map.put("roles", roles);
        map.put("admin", roles.contains("sup_admin"));
        map.put("name", user.getName());
        map.put("regionName", regionService.getById(LoginUserUtil.getUserRegion()).getRegionName());
        map.put("regionId", LoginUserUtil.getUserRegion());

        return RestResponse.ok(map);
    }

    // 普通用户、超级管理员
//    @PreAuthorize("hasAuthority('sys:user:list')")
    @PostMapping("/auth/pass")
    @ApiOperation(value = "密码加密 id传需要加密的密码")
    public RestResponse<String> pass(@RequestBody PublicIdReqVO publicIdReqVO) {

        // 加密后密码
        String password = bCryptPasswordEncoder.encode(publicIdReqVO.getId());

        boolean matches = bCryptPasswordEncoder.matches("111111", password);

        System.out.println("匹配结果：" + matches);

        return RestResponse.ok(password);
    }

}

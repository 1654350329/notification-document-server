package com.tree.clouds.notification.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tree.clouds.notification.common.RestResponse;
import com.tree.clouds.notification.common.aop.Log;
import com.tree.clouds.notification.model.entity.Region;
import com.tree.clouds.notification.service.RegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/* <p>
 * 指标制定表 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@RestController
@RequestMapping("/region")
@Api(value = "region", tags = "地区模块")
public class RegionController {
    @Autowired
    private RegionService regionService;


    @PostMapping("/getRegion")
    @ApiOperation(value = "获取地区信息")
    @Log("获取地区信息")
    public RestResponse<List<Region>> getRegion() {
        List<Region> list = regionService.list(new QueryWrapper<Region>().orderByAsc(Region.REGION_ID));
        return RestResponse.ok(list);
    }
}

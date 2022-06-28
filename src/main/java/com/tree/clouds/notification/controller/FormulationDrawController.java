package com.tree.clouds.notification.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.notification.common.RestResponse;
import com.tree.clouds.notification.common.aop.Log;
import com.tree.clouds.notification.model.entity.FormulationDraw;
import com.tree.clouds.notification.model.vo.FormulationDrawPageVO;
import com.tree.clouds.notification.model.vo.FormulationDrawVO;
import com.tree.clouds.notification.model.vo.PublicIdReqVO;
import com.tree.clouds.notification.service.FormulationDrawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 指标制定表 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@RestController
@RequestMapping("/formulation-draw")
@Api(value = "formulation-draw", tags = "指标制定模块")
public class FormulationDrawController {
    @Autowired
    private FormulationDrawService formulationDrawService;

    @PostMapping("/drawPage")
    @ApiOperation(value = "工作指标与工作拆解分页查询")
    @Log("指标制定分页查询")
    @PreAuthorize("hasAuthority('formulation:draw:list')")
    public RestResponse<IPage<FormulationDraw>> loginLogPage(@RequestBody FormulationDrawPageVO formulationDrawPageVO) {
        IPage<FormulationDraw> page = formulationDrawService.drawPage(formulationDrawPageVO);
        return RestResponse.ok(page);
    }

    @PostMapping("/addDraw")
    @ApiOperation(value = "添加指标制定方案")
    @Log("添加指标制定")
    @PreAuthorize("hasAuthority('formulation:draw:add')")
    public RestResponse<Boolean> addDraw(@Validated @RequestBody FormulationDrawVO formulationDrawVO) {
        formulationDrawService.addDraw(formulationDrawVO);
        return RestResponse.ok(true);
    }

    @PostMapping("/updateDraw")
    @ApiOperation(value = "修改指标制定方案")
    @Log("修改指标制定")
    @PreAuthorize("hasAuthority('formulation:draw:update')")
    public RestResponse<Boolean> updateDraw(@RequestBody FormulationDrawVO formulationDrawVO) {
        formulationDrawService.updateDraw(formulationDrawVO);
        return RestResponse.ok(true);
    }

    @PostMapping("/updateSort/{drawId}/{sort}")
    @ApiOperation(value = "修改排序")
    @Log("修改排序")
    @PreAuthorize("hasAuthority('formulation:draw:update')")
    public RestResponse<Boolean> updateSort(@PathVariable String drawId, @PathVariable Integer sort) {
        formulationDrawService.updateSort(drawId, sort);
        return RestResponse.ok(true);
    }

    @GetMapping("/importDraw/{year}")
    @ApiOperation(value = "导出指标方案")
    @Log("导出指标方案")
    public void importDraw(HttpServletResponse response, @PathVariable Integer year) {
        formulationDrawService.exportDraw(year, response);
    }

    @PostMapping("/copyDraw")
    @ApiOperation(value = "复制指标制定方案 参数为年份如 2022")
    @Log("复制指标制定方案")
    @PreAuthorize("hasAuthority('formulation:draw:copy')")
    public RestResponse<Boolean> copyDraw(@RequestBody PublicIdReqVO publicIdReqVO) {
        formulationDrawService.copyDraw(Integer.valueOf(publicIdReqVO.getId()));
        return RestResponse.ok(true);
    }

    @PostMapping("/removeDraw")
    @ApiOperation(value = "删除指标制定方案")
    @Log("删除指标制定方案")
    @PreAuthorize("hasAuthority('formulation:draw:remove')")
    public RestResponse<Boolean> removeDraw(@RequestBody PublicIdReqVO publicIdReqVO) {
        formulationDrawService.removeDraw(publicIdReqVO.getId());
        return RestResponse.ok(true);
    }

    @PostMapping("/getDrawInfo")
    @ApiOperation(value = "指标制定方案详情")
    @Log("指标制定方案详情")
    @PreAuthorize("hasAuthority('formulation:draw:detail')")
    public RestResponse<FormulationDraw> getDrawInfo(@RequestBody PublicIdReqVO publicIdReqVO) {
        FormulationDraw formulationDraw = formulationDrawService.getDrawInfo(publicIdReqVO.getId());
        return RestResponse.ok(formulationDraw);
    }
}


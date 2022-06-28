package com.tree.clouds.notification.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.notification.common.RestResponse;
import com.tree.clouds.notification.common.aop.Log;
import com.tree.clouds.notification.model.entity.FormulationLog;
import com.tree.clouds.notification.model.vo.FormulationLogPageVO;
import com.tree.clouds.notification.model.vo.FormulationLogVO;
import com.tree.clouds.notification.model.vo.PublicIdReqVO;
import com.tree.clouds.notification.service.FormulationLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-01-11
 */
@RestController
@RequestMapping("/formulation-log")
@Api(value = "formulation-log", tags = "通报日志模块")
public class FormulationLogController {
    @Autowired
    private FormulationLogService formulationLogService;

    @PostMapping("/formulationLogPage")
    @ApiOperation(value = "通报日志分页查询")
    @Log("通报日志分页查询")
    @PreAuthorize("hasAuthority('formulation:log:list')")
    public RestResponse<IPage<FormulationLog>> formulationLogPage(@RequestBody FormulationLogPageVO formulationLogPageVO) {
        IPage<FormulationLog> page = formulationLogService.formulationLogPage(formulationLogPageVO);
        return RestResponse.ok(page);
    }

    @PostMapping("/formulationLogDetail")
    @ApiOperation(value = "通报日志查看详情")
    @Log("通报日志查看详情")
    @PreAuthorize("hasAuthority('formulation:log:detail')")
    public RestResponse<FormulationLogVO> formulationLogDetail(@RequestBody PublicIdReqVO publicIdReqVO) {
        FormulationLogVO formulationLogVO = formulationLogService.formulationLogDetail(publicIdReqVO.getId());
        return RestResponse.ok(formulationLogVO);
    }

    @GetMapping("/exportFormulationLog")
    @ApiOperation(value = "通报日志导出")
    @Log("通报日志导出")
//    @PreAuthorize("hasAuthority('formulation:log:export')")
    public void exportFormulationLog(PublicIdReqVO publicIdReqVO, HttpServletResponse response) {
        formulationLogService.exportFormulationLog(publicIdReqVO.getId(), response);
    }

    @GetMapping("/exportLog/{logId}/{drawId}")
    @ApiOperation(value = "导出指定日志")
    @Log("通报日志导出")
//    @PreAuthorize("hasAuthority('formulation:log:export')")
    public void exportLog(@PathVariable String logId, @PathVariable String drawId, HttpServletResponse response) {
        formulationLogService.exportLog(logId, drawId, response);
    }
}


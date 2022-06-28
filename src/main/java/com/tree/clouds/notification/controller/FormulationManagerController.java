package com.tree.clouds.notification.controller;


import com.tree.clouds.notification.common.RestResponse;
import com.tree.clouds.notification.common.aop.Log;
import com.tree.clouds.notification.model.vo.DataInfoVO;
import com.tree.clouds.notification.service.DataReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 通报文件管理模块
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@RestController
@RequestMapping("/formulation-manager")
@Api(value = "formulation-manager", tags = "通报文件管理模块")
public class FormulationManagerController {
    @Autowired
    private DataReportService dataReportService;

    @PostMapping("/addReleaseExamine/{year}/{month}")
    @ApiOperation(value = "数据通报")
    @Log("数据通报")
    @PreAuthorize("hasAuthority('formulation:manager:add')")
    public RestResponse<Boolean> addReleaseExamine(@PathVariable Integer year, @PathVariable Integer month) {
        if (month == 0) {
            year = year - 1;
            month = 12;
        }
        dataReportService.addReleaseExamine(year, month);
        return RestResponse.ok(true);
    }

    @PostMapping("/dataInfo/{year}/{month}")
    @ApiOperation(value = "通报数据展示")
    @Log("数据通报")
    @PreAuthorize("hasAuthority('formulation:manager:dataInfo')")
    public RestResponse<List<DataInfoVO>> dataInfo(@PathVariable int year, @PathVariable int month) {
        if (month == 0) {
            year = year - 1;
            month = 12;
        }
        List<DataInfoVO> dataInfoVOS = dataReportService.dataInfo(year, month, null);
        return RestResponse.ok(dataInfoVOS);
    }

    @GetMapping("/exportLog/{drawId}/{month}")
    @ApiOperation(value = "指定项目导出")
    @Log("通报文件导出")
//    @PreAuthorize("hasAuthority('formulation:manager:export')")
    public void exportLog(@PathVariable String drawId, @PathVariable Integer month, HttpServletResponse response) {
        if (month == 0) {
            month = 1;
        }
        dataReportService.exportLog(drawId, month, response);
    }

    @GetMapping("/exportFormulationLog/{year}/{month}")
    @ApiOperation(value = "通报文件导出")
    @Log("通报文件导出")
//    @PreAuthorize("hasAuthority('formulation:manager:export')")
    public void exportFormulationLog(@PathVariable int year, @PathVariable int month, HttpServletResponse response) {
        if (month == 0) {
            year = year - 1;
            month = 12;
        }
        dataReportService.exportFormulationLog(year, month, response);
    }
}

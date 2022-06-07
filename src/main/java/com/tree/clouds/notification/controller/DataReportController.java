package com.tree.clouds.notification.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.notification.common.RestResponse;
import com.tree.clouds.notification.common.aop.Log;
import com.tree.clouds.notification.model.entity.DataReport;
import com.tree.clouds.notification.model.vo.DataReportVO;
import com.tree.clouds.notification.model.vo.FormulationDrawPageVO;
import com.tree.clouds.notification.service.DataReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 数据上报 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@RestController
@RequestMapping("/data-report")
@Api(value = "data-report", tags = "数据上报模块")
public class DataReportController {
    @Autowired
    private DataReportService dataReportService;

    @PostMapping("/dataReportPage")
    @ApiOperation(value = "数据上报分页查询")
    @Log("数据上报分页查询")
    @PreAuthorize("hasAuthority('data:report:list')")
    public RestResponse<IPage<DataReport>> dataReportPage(@RequestBody FormulationDrawPageVO drawPageVO) {
        if (drawPageVO.getRegionId() != null && drawPageVO.getRegionId() == -1) {
            drawPageVO.setRegionId(null);
        }
        IPage<DataReport> page = dataReportService.dataReportPage(drawPageVO, drawPageVO.getRegionId());
        return RestResponse.ok(page);
    }

    @PostMapping("/addDataReport")
    @ApiOperation(value = "数据上报")
    @Log("数据上报")
    @PreAuthorize("hasAuthority('data:report:add')")
    public RestResponse<Boolean> addDataReport(@RequestBody DataReportVO dataReportVO) {
        dataReportService.addDataReport(dataReportVO);
        return RestResponse.ok(true);
    }

    @GetMapping("/exportDataReport/{year}/{regionId}")
    @ApiOperation(value = "导出上报数据")
    @Log("导出上报数据")
//    @PreAuthorize("hasAuthority('data:report:export')")
    public void exportDataReport(@PathVariable Integer year, @PathVariable Integer regionId, HttpServletResponse response) {
        dataReportService.exportDataReport(year, regionId, response);
    }
}


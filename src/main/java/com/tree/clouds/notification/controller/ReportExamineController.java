package com.tree.clouds.notification.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.notification.common.RestResponse;
import com.tree.clouds.notification.common.aop.Log;
import com.tree.clouds.notification.model.entity.MonthInfo;
import com.tree.clouds.notification.model.entity.ReportRecord;
import com.tree.clouds.notification.model.vo.*;
import com.tree.clouds.notification.service.ReportExamineService;
import com.tree.clouds.notification.service.ReportRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 上报与审核中间表 前端控制器
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@RestController
@RequestMapping("/report-examine")
@Api(value = "report-examine", tags = "数据初审复核模块")
public class ReportExamineController {
    @Autowired
    private ReportExamineService reportExamineService;

    @Autowired
    private ReportRecordService reportRecordService;

    @PostMapping("/reportExaminePage")
    @ApiOperation(value = "数据初审分页查询")
    @Log("数据初审分页查询")
    @PreAuthorize("hasAuthority('report:examine:list')")
    public RestResponse<IPage<ReportExamineVO>> reportExaminePage(@RequestBody ReportExaminePageVO reportExaminePageVO) {
        if (reportExaminePageVO.getRegionId() != null && reportExaminePageVO.getRegionId() == -1) {
            reportExaminePageVO.setRegionId(null);
        }
        IPage<ReportExamineVO> page = reportExamineService.reportExaminePage(reportExaminePageVO);
        return RestResponse.ok(page);
    }

    @PostMapping("/reviewExaminePage")
    @ApiOperation(value = "数据复核分页查询")
    @Log("数据复核分页查询")
    @PreAuthorize("hasAuthority('report:examine:review')")
    public RestResponse<IPage<ReportExamineVO>> reviewExaminePage(@RequestBody ReportExaminePageVO reportExaminePageVO) {
        if (reportExaminePageVO.getRegionId() != null && reportExaminePageVO.getRegionId() == -1) {
            reportExaminePageVO.setRegionId(null);
        }
        IPage<ReportExamineVO> page = reportExamineService.reviewExaminePage(reportExaminePageVO);
        return RestResponse.ok(page);
    }

    @PostMapping("/addReportExamine")
    @ApiOperation(value = "数据初审保存")
    @Log("数据初审保存")
    @PreAuthorize("hasAuthority('formulation:examine:add')")
    public RestResponse<Boolean> addReportExamine(@RequestBody AddReportExamineVO addReportExamineVO) {
        reportExamineService.addReportExamine(addReportExamineVO, MonthInfo.EXAMINE_STATUS_TWO);
        return RestResponse.ok(true);
    }

    @PostMapping("/addReviewExamine")
    @ApiOperation(value = "数据复核保存")
    @Log("数据复核保存")
    @PreAuthorize("hasAuthority('formulation:examine:save')")
    public RestResponse<Boolean> addReviewExamine(@RequestBody AddReportExamineVO addReportExamineVO) {
        reportExamineService.addReportExamine(addReportExamineVO, MonthInfo.EXAMINE_STATUS_THREE);
        return RestResponse.ok(true);
    }

    @PostMapping("/updateData")
    @ApiOperation(value = "数据复核修改")
    @Log("数据复核修改")
    @PreAuthorize("hasAuthority('report:examine:update')")
    public RestResponse<Boolean> updateData(@RequestBody UpdateDataVO updateDataVO) {
        reportExamineService.updateData(updateDataVO);
        return RestResponse.ok(true);
    }

    @PostMapping("/batchReview")
    @ApiOperation(value = "数据批量复核通过")
    @Log("数据批量复核通过")
    @PreAuthorize("hasAuthority('report:examine:batch')")
    public RestResponse<Boolean> batchReview(@Validated @RequestBody List<BatchReviewVO> batchReviews) {
        for (BatchReviewVO batchReview : batchReviews) {
            AddReportExamineVO addReportExamineVO = new AddReportExamineVO();
            addReportExamineVO.setMonthId(batchReview.getMonthId());
            addReportExamineVO.setStatus(1);
            addReportExamineVO.setUpdateTime(batchReview.getUpdateTime());
            reportExamineService.addReportExamine(addReportExamineVO, MonthInfo.EXAMINE_STATUS_THREE);
        }
        return RestResponse.ok(true);
    }

    @PostMapping("/getHistoryRecord")
    @ApiOperation(value = "报送历史 id为 monthId")
    @Log("报送历史")
    @PreAuthorize("hasAuthority('report:examine:get')")
    public RestResponse<List<ReportRecord>> getHistoryRecord(@Validated @RequestBody PublicIdReqVO publicIdReqVO) {
        return RestResponse.ok(this.reportRecordService.getHistoryRecord(publicIdReqVO.getId()));
    }
}


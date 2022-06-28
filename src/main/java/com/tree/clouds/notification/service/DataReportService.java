package com.tree.clouds.notification.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.notification.model.entity.DataReport;
import com.tree.clouds.notification.model.vo.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 数据上报 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
public interface DataReportService extends IService<DataReport> {

    IPage<DataReport> dataReportPage(FormulationDrawPageVO drawPageVO, Integer regionId);

    void addDataReport(DataReportVO dataReportVO);

    IPage<ReportExamineVO> reportExaminePage(IPage<ReportExamineVO> page, ReportExaminePageVO reportExaminePageVO);

    void addReleaseExamine(Integer year, Integer month);

    List<DataInfoVO> dataInfo(Integer year, Integer month, String drawId);

    void exportFormulationLog(int year, int month, HttpServletResponse response);

    void exportDataReport(Integer year, Integer regionId, HttpServletResponse response);

    IPage<ReportExamineVO> reviewExaminePage(IPage<ReportExamineVO> page, ReportExaminePageVO reportExaminePageVO);

    DataReport getByDisassembleIdAndRegionId(String disassembleId, Integer regionId);

    void exportLog(String drawId, Integer month, HttpServletResponse response);
}

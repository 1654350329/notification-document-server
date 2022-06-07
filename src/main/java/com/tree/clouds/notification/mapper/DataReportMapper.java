package com.tree.clouds.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.notification.model.entity.DataReport;
import com.tree.clouds.notification.model.entity.MonthInfo;
import com.tree.clouds.notification.model.vo.FormulationDrawPageVO;
import com.tree.clouds.notification.model.vo.ReportExaminePageVO;
import com.tree.clouds.notification.model.vo.ReportExamineVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据上报 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
public interface DataReportMapper extends BaseMapper<DataReport> {

    IPage<DataReport> dataReportPage(IPage<DataReport> page, @Param("formulationDrawPageVO") FormulationDrawPageVO formulationDrawPageVO, @Param("regionId") Integer regionId);

    IPage<ReportExamineVO> reportExaminePage(IPage<ReportExamineVO> page, @Param("reportExaminePageVO") ReportExaminePageVO reportExaminePageVO, @Param("regionId") Integer regionId);

    List<MonthInfo> listByDate(@Param("year") int year, @Param("month") int month);

    IPage<ReportExamineVO> reviewExaminePage(IPage<ReportExamineVO> page, @Param("reportExaminePageVO") ReportExaminePageVO reportExaminePageVO);
}

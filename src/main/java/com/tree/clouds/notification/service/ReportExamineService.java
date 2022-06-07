package com.tree.clouds.notification.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.notification.model.entity.ReportExamine;
import com.tree.clouds.notification.model.vo.AddReportExamineVO;
import com.tree.clouds.notification.model.vo.ReportExaminePageVO;
import com.tree.clouds.notification.model.vo.ReportExamineVO;
import com.tree.clouds.notification.model.vo.UpdateDataVO;

/**
 * <p>
 * 上报与审核中间表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
public interface ReportExamineService extends IService<ReportExamine> {

    IPage<ReportExamineVO> reportExaminePage(ReportExaminePageVO reportExaminePageVO);

    void addReportExamine(AddReportExamineVO addReportExamineVO, int progress);

    IPage<ReportExamineVO> reviewExaminePage(ReportExaminePageVO reportExaminePageVO);


    void updateData(UpdateDataVO updateDataVO);
}

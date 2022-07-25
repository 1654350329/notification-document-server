package com.tree.clouds.notification.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.notification.mapper.ReportExamineMapper;
import com.tree.clouds.notification.model.entity.MonthInfo;
import com.tree.clouds.notification.model.entity.ReportExamine;
import com.tree.clouds.notification.model.vo.AddReportExamineVO;
import com.tree.clouds.notification.model.vo.ReportExaminePageVO;
import com.tree.clouds.notification.model.vo.ReportExamineVO;
import com.tree.clouds.notification.model.vo.UpdateDataVO;
import com.tree.clouds.notification.service.DataReportService;
import com.tree.clouds.notification.service.MonthInfoService;
import com.tree.clouds.notification.service.ReportExamineService;
import com.tree.clouds.notification.service.ReportRecordService;
import com.tree.clouds.notification.utils.BaseBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * <p>
 * 上报与审核中间表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Service
public class ReportExamineServiceImpl extends ServiceImpl<ReportExamineMapper, ReportExamine> implements ReportExamineService {

    @Autowired
    private DataReportService dataReportService;
    @Autowired
    private MonthInfoService monthInfoService;
    @Autowired
    private ReportRecordService reportRecordService;

    @Override
    public IPage<ReportExamineVO> reportExaminePage(ReportExaminePageVO reportExaminePageVO) {
        IPage<ReportExamineVO> page = reportExaminePageVO.getPage();
        page = dataReportService.reportExaminePage(page, reportExaminePageVO);
        return page;
    }

    @Override
    @Transactional
    public void addReportExamine(AddReportExamineVO addReportExamineVO, int progress) {
        if (addReportExamineVO.getRemark() != null && addReportExamineVO.getRemark().length() > 300) {
            throw new BaseBusinessException(400, "审核意见不许超过300字符!");
        }
        MonthInfo info = monthInfoService.getById(addReportExamineVO.getMonthId());
        if (info.getExamineStatus() >= 3 && info.getStatus() == 1) {
            return;
        }
        if (progress == 3 && info.getStatus() != null && info.getStatus() == 2) {
            throw new BaseBusinessException(400, "数据处于驳回状态,无法通过");
        }
        if (info.getUpdateTime() != null && !info.getUpdateTime().equals(addReportExamineVO.getUpdateTime())) {
            throw new BaseBusinessException(400, "数据不是最新,请重新刷新数据");
        }
        MonthInfo monthInfo = BeanUtil.toBean(addReportExamineVO, MonthInfo.class);
        monthInfo.setMonth(info.getMonth());
        monthInfo.setExamineStatus(progress);

        if (progress == 3) {
            monthInfo.setRemark(addReportExamineVO.getRemark());
            reportRecordService.addRecord(Collections.singletonList(monthInfo.getMonthId()), "复核");
        } else {
            monthInfo.setStatus(0);
            reportRecordService.addRecord(Collections.singletonList(monthInfo.getMonthId()), "初审");
        }
        monthInfoService.updateById(monthInfo);

    }

    @Override
    public IPage<ReportExamineVO> reviewExaminePage(ReportExaminePageVO reportExaminePageVO) {
        IPage<ReportExamineVO> page = reportExaminePageVO.getPage();
        page = dataReportService.reviewExaminePage(page, reportExaminePageVO);
        return page;
    }

    @Override
    public void updateData(UpdateDataVO updateDataVO) {
        MonthInfo monthInfo = BeanUtil.toBean(updateDataVO, MonthInfo.class);
        monthInfo.setExamineStatus(2);
        monthInfo.setStatus(0);
        monthInfoService.updateById(monthInfo);
        reportRecordService.addRecord(Collections.singletonList(monthInfo.getMonthId()), "修改");
    }

}

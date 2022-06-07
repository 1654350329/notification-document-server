package com.tree.clouds.notification.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.notification.mapper.ReportRecordMapper;
import com.tree.clouds.notification.model.entity.ReportRecord;
import com.tree.clouds.notification.service.ReportRecordService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-02-25
 */
@Service
public class ReportRecordServiceImpl extends ServiceImpl<ReportRecordMapper, ReportRecord> implements ReportRecordService {

    @Override
    public Boolean addRecord(List<String> monthIds, String examineStatus) {
        List<ReportRecord> reportRecords = new ArrayList<>();
        for (String monthId : monthIds) {
            ReportRecord reportRecord = new ReportRecord();
            reportRecord.setExamineStatus(examineStatus);
            reportRecord.setMonthId(monthId);
            reportRecords.add(reportRecord);
        }
        return this.saveBatch(reportRecords);
    }

    @Override
    public List<ReportRecord> getHistoryRecord(String id) {

        return this.baseMapper.getHistoryRecord(id);
    }
}

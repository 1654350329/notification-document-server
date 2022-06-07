package com.tree.clouds.notification.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.notification.model.entity.ReportRecord;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-02-25
 */
public interface ReportRecordService extends IService<ReportRecord> {

    Boolean addRecord(List<String> monthIds, String examineStatus);

    List<ReportRecord> getHistoryRecord(String id);
}

package com.tree.clouds.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.notification.model.entity.ReportRecord;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-02-25
 */
public interface ReportRecordMapper extends BaseMapper<ReportRecord> {

    List<ReportRecord> getHistoryRecord(String monthId);
}

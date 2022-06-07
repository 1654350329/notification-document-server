package com.tree.clouds.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.notification.model.entity.MonthInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 月份数据 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-01-07
 */
public interface MonthInfoMapper extends BaseMapper<MonthInfo> {


    List<String> getDataByMonth(@Param("bizId") String bizId, @Param("month") int month, @Param("type") int type);

    void updateExamineStatus(@Param("ids") List<String> ids);

    Long getDataSum(@Param("ids") List<String> ids, @Param("month") int month);

}

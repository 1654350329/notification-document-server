package com.tree.clouds.notification.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.notification.model.entity.MonthInfo;
import com.tree.clouds.notification.model.vo.MonthInfoVO;

import java.util.List;

/**
 * <p>
 * 月份数据 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-01-07
 */
public interface MonthInfoService extends IService<MonthInfo> {

    void addByBizId(List<MonthInfoVO> monthInfoVOS, String drawId);

    List<MonthInfoVO> getByBizId(String drawId);

    void updateByBizId(List<MonthInfoVO> monthInfoVOS, String drawId);

    void updateByMonthId(List<MonthInfoVO> monthInfoVOS);

    void deleteByBizId(String bizId);

    void copyData(String oldBizId, String newBizId);

    String getDataByMonth(String bizId, int month);

    double getDataByMonth(String regionId, int month, int type);

    /**
     * 更新完成通报审核
     *
     * @param ids
     */
    void updateExamineStatus(List<String> ids);

    String getDataSum(List<String> reportIds, int month);
}

package com.tree.clouds.notification.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.notification.mapper.MonthInfoMapper;
import com.tree.clouds.notification.model.entity.MonthInfo;
import com.tree.clouds.notification.model.vo.MonthInfoVO;
import com.tree.clouds.notification.service.MonthInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 月份数据 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-01-07
 */
@Service
public class MonthInfoServiceImpl extends ServiceImpl<MonthInfoMapper, MonthInfo> implements MonthInfoService {

    @Override
    @Transactional
    public void addByBizId(List<MonthInfoVO> monthInfoVOS, String bizId) {
        List<MonthInfo> monthInfos = new ArrayList<>();
        if (CollUtil.isEmpty(monthInfoVOS)) {
            for (int i = 1; i <= 12; i++) {
                MonthInfo monthInfo = new MonthInfo();
                monthInfo.setBizId(bizId);
                monthInfo.setData("/");
                monthInfo.setMonth(i);
                monthInfos.add(monthInfo);
            }
        } else {
            monthInfos = monthInfoVOS.stream().map(monthInfoVO -> {
                MonthInfo monthInfo = BeanUtil.toBean(monthInfoVO, MonthInfo.class);
                monthInfo.setBizId(bizId);
                return monthInfo;
            }).collect(Collectors.toList());
        }

        this.saveBatch(monthInfos);
    }

    @Override
    public List<MonthInfoVO> getByBizId(String bizId) {
        QueryWrapper<MonthInfo> wrapper = new QueryWrapper<>();
        wrapper.eq(MonthInfo.BIZ_ID, bizId);
        List<MonthInfo> monthInfoList = this.list(wrapper);
        return monthInfoList.stream().map(monthInfo -> BeanUtil.toBean(monthInfo, MonthInfoVO.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateByBizId(List<MonthInfoVO> monthInfoVOS, String bizId) {
        deleteByBizId(bizId);
        addByBizId(monthInfoVOS, bizId);
    }

    @Override
    @Transactional
    public void updateByMonthId(List<MonthInfoVO> monthInfoVOS) {
        List<MonthInfo> monthInfos = new ArrayList<>();
        for (MonthInfoVO monthInfoVO : monthInfoVOS) {
            MonthInfo monthInfo = BeanUtil.toBean(monthInfoVO, MonthInfo.class);
            monthInfos.add(monthInfo);
        }
        this.updateBatchById(monthInfos);
    }

    @Override
    public void deleteByBizId(String bizId) {
        QueryWrapper<MonthInfo> wrapper = new QueryWrapper<>();
        wrapper.eq(MonthInfo.BIZ_ID, bizId);
        this.remove(wrapper);
    }

    @Override
    public void copyData(String oldBizId, String newBizId) {
        List<MonthInfoVO> infoVOS = getByBizId(oldBizId);
        addByBizId(infoVOS, newBizId);
    }

    @Override
    public String getDataByMonth(String bizId, int month) {
        List<String> dataByMonth = this.baseMapper.getDataByMonth(bizId, month, 0);
        if (CollUtil.isNotEmpty(dataByMonth)) {
            return dataByMonth.get(0);
        }
        return null;
    }

    @Override
    public double getDataByMonth(String regionId, int month, int type) {
        List<String> dataByMonth = this.baseMapper.getDataByMonth(regionId, month, type);
        List<Double> longs = new ArrayList<>();
        for (String data : dataByMonth) {
            if (!NumberUtil.isNumber(data)) {
                continue;
            }
            longs.add(Double.parseDouble(data));
        }
        double sum = 0.000;
        for (Double aLong : longs) {
            sum += aLong;
        }
        if (CollUtil.isEmpty(dataByMonth)) {
            return 0.000;
        }
        DecimalFormat df = new DecimalFormat("0.000");
        String format = df.format((sum) / 100);
        String percent = NumberUtil.formatPercent(Double.parseDouble(format) / dataByMonth.size(), 2);
        return Double.parseDouble(percent.substring(0, percent.length() - 1).replace(",", ""));
    }

    @Override
    public void updateExamineStatus(List<String> ids) {
        this.baseMapper.updateExamineStatus(ids);
    }

    @Override
    public String getDataSum(List<String> reportIds, int month) {
        Long dataSum = this.baseMapper.getDataSum(reportIds, month);
        if (dataSum == null) {
            return null;
        }
        return String.valueOf(dataSum);

    }

}

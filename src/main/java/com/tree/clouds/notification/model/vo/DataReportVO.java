package com.tree.clouds.notification.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DataReportVO {
    @ApiModelProperty(value = "各月份数据")
    List<MonthInfoVO> monthInfoVOS;
    @ApiModelProperty(value = "数据上报主键")
    private String reportId;

}

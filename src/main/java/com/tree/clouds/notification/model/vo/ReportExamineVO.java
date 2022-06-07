package com.tree.clouds.notification.model.vo;

import com.tree.clouds.notification.model.entity.MonthInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReportExamineVO extends MonthInfo {
    @ApiModelProperty(value = "指标拆解表id")
    private String disassembleId;
    @ApiModelProperty(value = "责任单位")
    private String regionName;

    @ApiModelProperty(value = "重点指标,重点项目名称")
    private String drawName;

    @ApiModelProperty(value = "全年任务")
    private String task;
    @ApiModelProperty("年份")
    private String year;

    @ApiModelProperty("序时进度")
    private String orderProgress;

    @ApiModelProperty("当月进度")
    private String monthProgress;

    @ApiModelProperty("进度")
    private String progress;
    @ApiModelProperty(value = "数值类型 0数字 1百分比 2文本")
    private Integer numberType;


}

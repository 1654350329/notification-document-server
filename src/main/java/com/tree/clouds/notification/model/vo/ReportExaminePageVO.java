package com.tree.clouds.notification.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReportExaminePageVO extends PageParam {
    @ApiModelProperty(value = "重点指标,重点项目名称")
    private String drawName;

    @ApiModelProperty(value = "责任单位")
    private String regionName;

    @ApiModelProperty(value = "责任单位id")
    private Integer regionId;

    @ApiModelProperty("年份")
    private String year;

    @ApiModelProperty("月份")
    private String month;

    @ApiModelProperty("状态 0初始 1待审核 2待复核 3已复核")
    private Integer examineStatus;

    @ApiModelProperty(value = "审核状态 1成功 2反驳")
    private Integer status;
}

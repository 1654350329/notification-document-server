package com.tree.clouds.notification.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DataInfoExcelVO {
    @ApiModelProperty(value = "指标制定id")
    private String drawId;
    @ApiModelProperty(value = "重点指标,重点项目")
    private String drawName;
    @ApiModelProperty(value = "责任领导")
    private String leader;
    @ApiModelProperty(value = "地区名称")
    private String regionName;
    @ApiModelProperty(value = "全年任务")
    private String task;
    @ApiModelProperty(value = "当月进度")
    private String data;
    @ApiModelProperty(value = "月份")
    private int month;
    @ApiModelProperty(value = "累计完成")
    private String dataSum;
    @ApiModelProperty(value = "进度")
    private String progress;
    @ApiModelProperty(value = "成效评价 1红 2蓝")
    private int status;
}

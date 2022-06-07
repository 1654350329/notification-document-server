package com.tree.clouds.notification.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RegionDataVO {
    @ApiModelProperty(value = "拆解id")
    private String disassembleId;
    @ApiModelProperty(value = "地区id")
    private Integer regionId;
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
    @ApiModelProperty(value = "成效评价 1红 0蓝")
    private Integer status;
    @ApiModelProperty(value = "排名")
    private Integer ranking;
}

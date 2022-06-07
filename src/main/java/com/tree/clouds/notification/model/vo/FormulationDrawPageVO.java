package com.tree.clouds.notification.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FormulationDrawPageVO extends PageParam {
    @ApiModelProperty(value = "年份")
    private Integer year;

    @ApiModelProperty(value = "项目名称")
    private String drawName;
    @ApiModelProperty(value = "地区id")
    private Integer regionId;

    @ApiModelProperty(value = "分配状态 0未分配 1已分配 2未分配完全")
    private Integer distributeStatus;

}

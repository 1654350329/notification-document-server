package com.tree.clouds.notification.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DataInfoVO {

    @ApiModelProperty(value = "各地区数据")
    List<RegionDataVO> regionDataVOS;
    @ApiModelProperty(value = "指标制定id")
    private String drawId;
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    @ApiModelProperty(value = "重点指标,重点项目")
    private String drawName;
    @ApiModelProperty(value = "责任领导")
    private String leader;
    @ApiModelProperty(value = "数值类型 0数字 1百分比 2文本")
    private int numberType;

    @ApiModelProperty(value = "计算类型 < ,<=, =, >,>=")
    private String calculationType;
}

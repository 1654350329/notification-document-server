package com.tree.clouds.notification.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

@Data
public class FormulationDisassembleVO {

    @ApiModelProperty(value = "各月数据")
    @Valid
    List<MonthInfoVO> monthInfoVOS;
    @ApiModelProperty(value = "指标拆解表id")
    private String disassembleId;
    @ApiModelProperty(value = "制定表主键")
    private String drawId;
    @ApiModelProperty(value = "地区id")
    private Integer regionId;
    @ApiModelProperty(value = "全年任务")
    private String task;
}

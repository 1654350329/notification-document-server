package com.tree.clouds.notification.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FormulationDrawVO {
    @ApiModelProperty(value = "各月份数据")
    @NotEmpty(message = "各月份数据不许为空")
    List<MonthInfoVO> monthInfoVOS;
    @ApiModelProperty(value = "指标制定id")
    private String drawId;
    @NotBlank(message = "项目名称不许为空")
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    @NotBlank(message = "指标项目名称不许为空")
    @ApiModelProperty(value = "重点指标,重点项目")
    private String drawName;
    //    @NotBlank(message = "排序")
    @ApiModelProperty(value = "排序")
    private Integer sort;
    //    @NotBlank(message = "责任领导不许为空")
    @ApiModelProperty(value = "责任领导")
    private String leader;
    @NotBlank(message = "全年任务不许为空")
    @ApiModelProperty(value = "全年任务")
    private String task;
    @NotNull(message = "数值类型不许为空")
    @ApiModelProperty(value = "数值类型 0数字 1百分比 2文本")
    private int numberType;
    //    @NotBlank(message = "计算类型不许为空")
    @ApiModelProperty(value = "计算类型 < ,<=, =, >,>=")
    private String calculationType;

}

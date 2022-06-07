package com.tree.clouds.notification.model.bo;

import com.tree.clouds.notification.model.vo.MonthInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class FormulationDisassembleBO {
    @ApiModelProperty(value = "各月份数据")
    List<MonthInfoVO> monthInfoVOS;
    @ApiModelProperty(value = "指标拆解id")
    private String disassembleId;
    @ApiModelProperty(value = "重点指标,重点项目")
    private String drawName;
    @ApiModelProperty(value = "项目名称")
    private String projectName;
    @ApiModelProperty(value = "责任领导")
    private String leader;
    @ApiModelProperty(value = "全年任务")
    private String task;
    @ApiModelProperty(value = "数值类型 0数字 1百分比 2文本")
    private int numberType;

    @ApiModelProperty(value = "计算类型 < ,<=, =, >,>=")
    private String calculationType;
}

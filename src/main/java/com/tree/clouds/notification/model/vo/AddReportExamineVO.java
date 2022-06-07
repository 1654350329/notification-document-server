package com.tree.clouds.notification.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddReportExamineVO {
    @ApiModelProperty(value = "数据上报主键 初审与复核")
    private String monthId;

    @ApiModelProperty(value = "上报数据")
    private String data;
    @ApiModelProperty(value = "更新时间")
    private String updateTime;

    @ApiModelProperty(value = "审核状态 1成功 2反驳 数据复核使用该字段")
    private Integer status;
    @ApiModelProperty(value = "审核意见 数据复核使用该字段")
    private String remark;

}

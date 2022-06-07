package com.tree.clouds.notification.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FormulationLogPageVO extends PageParam {
    @ApiModelProperty(value = "报送开始日期")
    private String formulationTimeStart;

    @ApiModelProperty(value = "报送结束日期")
    private String formulationTimeEnd;

    @ApiModelProperty(value = "标题")
    private String title;
}

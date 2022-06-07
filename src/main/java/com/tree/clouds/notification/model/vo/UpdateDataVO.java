package com.tree.clouds.notification.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UpdateDataVO {
    @NotBlank(message = "月份主键不许为空")
    @ApiModelProperty("月份主键")
    private String monthId;
    @NotBlank(message = "月份数据不许为空")
    @ApiModelProperty("月份数据")
    private String data;
}

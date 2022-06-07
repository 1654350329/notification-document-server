package com.tree.clouds.notification.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class BatchReviewVO {
    @ApiModelProperty("主键")
    @NotBlank(message = "主键不许为空")
    private String monthId;

    @NotBlank(message = "更新时间不许为空")
    @ApiModelProperty("更新时间")
    private String updateTime;
}

package com.tree.clouds.notification.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class MonthInfoVO {
    @ApiModelProperty(value = "月份主键")
    private String monthId;

    @ApiModelProperty(value = "月份")
    @Size(max = 12, min = 1, message = "最小月份为1月,最大月份12月")
    private Integer month;

    @ApiModelProperty(value = "数据")
    @NotNull
    private String data;

    @ApiModelProperty(value = "数据审核 0初始 1待审核 2已提交 3已复核 4通报 5完成")
    private int examineStatus;
}

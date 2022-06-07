package com.tree.clouds.notification.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserManagePageVO extends PageParam {
    @ApiModelProperty(value = "账号")
    private String account;
    @ApiModelProperty(value = "姓名")
    private String userName;

    @NotNull(message = "地区id不能为空")
    @ApiModelProperty(value = "地区id")
    private Integer regionId;
}

package com.tree.clouds.notification.model.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseEntity implements Serializable {
    public static final String CREATE_USER = "create_user";
    public static final String CREATE_TIME = "create_time";
    public static final String UPDATE_USER = "update_user";
    public static final String UPDATE_TIME = "update_time";
    public static final String DEL = "del";
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "创建人")
    @TableField(value = "create_user", fill = FieldFill.INSERT)
    @ExcelIgnore
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @ExcelIgnore
    private String createTime;

    @ApiModelProperty(value = "更新人")
    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    @ExcelIgnore
    private String updateUser;

    @ApiModelProperty(value = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @ExcelIgnore
    private String updateTime;

    @ApiModelProperty(value = "删除标识")
    @TableField(value = "del", fill = FieldFill.INSERT)
    @ExcelIgnore
//    @TableLogic
    private Integer del;

}

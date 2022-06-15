package com.tree.clouds.notification.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
@ApiModel(value = "User对象", description = "用户表")
public class User extends BaseEntity {

    public static final String USER_ID = "user_id";
    public static final String UNIT = "unit";
    public static final String NAME = "name";
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";
    public static final String STATUS = "status";
    public static final String PHONE = "phone";
    public static final String REGION_ID = "region_id";

    @ApiModelProperty(value = "用户id")
    @TableId(value = "user_id", type = IdType.UUID)
    private String userId;

    @ApiModelProperty(value = "单位")
    @TableField("unit")
    private String unit;

    @ApiModelProperty(value = "名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "账号")
    @TableField("account")
    private String account;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "密码更新时间")
    @TableField("PASSWORD_TIME")
    private String passwordTime;

    @ApiModelProperty(value = "账号状态 0停用 1启用")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "联系方式")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "地区主键")
    @TableField("region_id")
    private Integer regionId;


}

package com.tree.clouds.notification.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 分组与角色管理中间表
 * </p>
 *
 * @author LZK
 * @since 2022-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("region_user")
@ApiModel(value = "RegionUser对象", description = "分组与角色管理中间表")
public class RegionUser extends BaseEntity {
    public static final String REGION_ID = "region_ID";
    public static final String USER_ID = "user_ID";

    @ApiModelProperty(value = "分组id")
    @TableField("region_ID")
    private Integer regionId;

    @ApiModelProperty(value = "用户id")
    @TableField("user_ID")
    private String userId;


}

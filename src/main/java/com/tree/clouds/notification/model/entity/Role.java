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
 * 角色表
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("role")
@ApiModel(value = "Role对象", description = "角色表")
public class Role extends BaseEntity {
    public static final String ROLE_ID = "role_id";
    public static final String ROLE_NAME = "role_name";

    @ApiModelProperty(value = "角色主键")
    @TableId(value = "role_id", type = IdType.UUID)
    private String roleId;

    @ApiModelProperty(value = "角色名称")
    @TableField("role_name")
    private String roleName;

    @ApiModelProperty(value = "角色code")
    @TableField("ROLE_CODE")
    private String roleCode;


}

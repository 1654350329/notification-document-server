package com.tree.clouds.notification.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 角色菜单中间表
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("role_menu")
@ApiModel(value = "RoleMenu对象", description = "角色菜单中间表")
public class RoleMenu extends BaseEntity {
    public static final String ROLE_ID = "role_id";
    public static final String MENU_ID = "menu_id";

    @ApiModelProperty(value = "角色主键")
    @TableField("role_id")
    private String roleId;

    @ApiModelProperty(value = "菜单主键")
    @TableField("menu_id")
    private String menuId;


}

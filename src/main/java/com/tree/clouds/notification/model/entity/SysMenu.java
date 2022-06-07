package com.tree.clouds.notification.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_menu")
@ApiModel(value = "SysMenu对象", description = "")
public class SysMenu extends BaseEntity {

    public static final String ID = "id";
    public static final String PARENT_ID = "parent_id";
    public static final String NAME = "name";
    public static final String PATH = "path";
    public static final String PERMS = "perms";
    public static final String COMPONENT = "component";
    public static final String TYPE = "type";
    public static final String ICON = "icon";
    public static final String ORDER_NUM = "order_num";
    @TableId(value = "id", type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "父菜单ID，一级菜单为0")
    @TableField("parent_id")
    private String parentId;

    @TableField("name")
    private String name;

    @ApiModelProperty(value = "菜单URL")
    @TableField("path")
    private String path;

    @ApiModelProperty(value = "授权(多个用逗号分隔，如：user:list,user:create)")
    @TableField("perms")
    private String perms;

    @TableField("component")
    private String component;

    @ApiModelProperty(value = "类型 0：目录   1：菜单   2：按钮")
    @TableField("type")
    private Integer type;

    @ApiModelProperty(value = "菜单图标")
    @TableField("icon")
    private String icon;

    @ApiModelProperty(value = "排序")
    @TableField("order_num")
    private Integer orderNum;

    @TableField(exist = false)
    private List<SysMenu> children = new ArrayList<>();


}

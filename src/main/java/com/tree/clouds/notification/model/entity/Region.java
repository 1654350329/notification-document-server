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
 * 地区表
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("region")
@ApiModel(value = "Region对象", description = "地区表")
public class Region extends BaseEntity {

    public static final String REGION_ID = "region_id";
    public static final String REGION_NAME = "region_name";

    @ApiModelProperty(value = "地区主键")
    @TableId(value = "region_id", type = IdType.AUTO)
    private Integer regionId;

    @ApiModelProperty(value = "地区名称")
    @TableField("region_name")
    private String regionName;


}

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
 * 上报与审核中间表
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("report_examine")
@ApiModel(value = "ReportExamine对象", description = "上报与审核中间表")
public class ReportExamine extends BaseEntity {

    public static final String REPORT_ID = "report_id";
    public static final String STATUS = "status";

    @TableId(value = "report_id", type = IdType.UUID)
    @ApiModelProperty(value = "上报主键")
    private String reportId;

    @TableField("status")
    @ApiModelProperty(value = "审核状态")
    private Integer status;

}

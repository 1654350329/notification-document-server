package com.tree.clouds.notification.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author LZK
 * @since 2022-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("formulation_log")
@ApiModel(value = "FormulationLog对象", description = "")
public class FormulationLog extends BaseEntity {

    public static final String LOG_ID = "log_id";
    public static final String FORMULATION_TIME = "formulation_time";
    public static final String TITLE = "title";
    public static final String YEAR = "year";
    public static final String MONTH = "month";

    @ApiModelProperty(value = "通报日志主键")
    @TableId(value = "log_id", type = IdType.UUID)
    private String logId;

    @ApiModelProperty(value = "通报日期")
    @TableField("formulation_time")
    private String formulationTime;

    @ApiModelProperty(value = "标题")
    @TableField("title")
    private String title;

    @ApiModelProperty(value = "年")
    @TableField("year")
    private Integer year;

    @ApiModelProperty(value = "月")
    @TableField("month")
    private Integer month;

    @ApiModelProperty(value = "各月份数据")
    @TableField("data_Info")
    @JsonIgnore
    private String dataInfo;
}

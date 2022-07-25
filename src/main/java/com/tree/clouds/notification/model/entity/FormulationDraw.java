package com.tree.clouds.notification.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.tree.clouds.notification.model.vo.MonthInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 指标制定表
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("formulation_draw")
@ApiModel(value = "FormulationDraw对象", description = "指标制定表")
public class FormulationDraw extends BaseEntity {
    public static final String DRAW_ID = "draw_id";
    public static final String DRAW_NAME = "draw_name";
    public static final String LEADER = "leader";
    public static final String TASK = "task";
    public static final String YEAR = "year";
    public static final String SORT = "sort";
    public static final String STATUS = "status";
    @ApiModelProperty(value = "各月份信息")
    @TableField(exist = false)
    List<MonthInfoVO> monthInfoVOS;
    @ApiModelProperty(value = "指标制定id")
    @TableId(value = "draw_id", type = IdType.UUID)
    private String drawId;
    @ApiModelProperty(value = "项目名称")
    @TableField("project_name")
    private String projectName;
    @ApiModelProperty(value = "重点指标,重点项目")
    @TableField("draw_name")
    private String drawName;
    @ApiModelProperty(value = "责任领导")
    @TableField("leader")
    private String leader;
    @TableField("sort")
    private Integer sort;
    @ApiModelProperty(value = "全年任务")
    @TableField("task")
    private String task;
    @ApiModelProperty(value = "年份")
    @TableField("year")
    private Integer year;
    @ApiModelProperty(value = "发布状态 0未发布 1已发布")
    @TableField("status")
    private Integer status;
    @ApiModelProperty(value = "分配状态 0未分配 1已分配 2未完全分配")
    @TableField("distribute_status")
    private Integer distributeStatus;
    @ApiModelProperty(value = "数值类型 0数字 1百分比 2文本")
    @TableField("number_type")
    private Integer numberType;
    @ApiModelProperty(value = "计算类型 < ,<=, =, >,>=")
    @TableField(value = "calculation_type", updateStrategy = FieldStrategy.IGNORED)
    private String calculationType;
    @ApiModelProperty(value = "已分配单位")
    @TableField(exist = false)
    private int distribution;
    @ApiModelProperty(value = "未分配单位")
    @TableField(exist = false)
    private int unDistribution;


}

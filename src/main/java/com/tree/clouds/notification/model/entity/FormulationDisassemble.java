package com.tree.clouds.notification.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tree.clouds.notification.model.vo.MonthInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 指标拆解表
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("formulation_disassemble")
@ApiModel(value = "FormulationDisassemble对象", description = "指标拆解表")
public class FormulationDisassemble extends BaseEntity {
    public static final String DISASSEMBLE_ID = "disassemble_id";
    public static final String DRAW_ID = "draw_id";
    public static final String REGION_ID = "region_id";
    public static final String TASK = "task";
    public static final String STATUS = "status";
    public static final String RELEASE_STATUS = "release_status";
    @ApiModelProperty(value = "各月数据")
    @TableField(exist = false)
    @Valid
    List<MonthInfoVO> monthInfoVOS;
    @ApiModelProperty(value = "指标拆解表id")
    @TableId(value = "disassemble_id", type = IdType.UUID)
    private String disassembleId;
    @ApiModelProperty(value = "制定表主键")
    @TableField("draw_id")
    private String drawId;
    @ApiModelProperty(value = "地区id")
    @TableField("region_id")
    private Integer regionId;
    @ApiModelProperty(value = "地区")
    @TableField(exist = false)
    private String regionName;
    @ApiModelProperty(value = "分配状态 0未分配 1已分配")
    @TableField("status")
    private int status;
    @ApiModelProperty(value = "发布状态 0未发布 1已发布")
    @TableField("release_status")
    private int releaseStatus;
    @ApiModelProperty(value = "全年任务")
    @TableField("task")
    private String task;
}

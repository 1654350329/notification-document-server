package com.tree.clouds.notification.model.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tree.clouds.notification.model.vo.MonthInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 数据上报
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("data_report")
@ApiModel(value = "DataReport对象", description = "数据上报")
@ColumnWidth(20)
public class DataReport extends BaseEntity {
    public static final String REPORT_ID = "report_id";
    public static final String DISASSEMBLE_ID = "disassemble_id";
    public static final String TASK = "task";
    public static final String PROGRESS = "progress";
    public static final String EXAMINE_STATUS = "examine_status";
    public static final String REGION_ID = "region_id";
    public static final String YEAR = "year";
    public static final String MONTH = "month";
    @ApiModelProperty(value = "各月份数据")
    @TableField(exist = false)
    @ExcelIgnore
    List<MonthInfoVO> monthInfoVOS;
    @ApiModelProperty(value = "数据上报主键")
    @TableId(value = "report_id", type = IdType.UUID)
    @ExcelIgnore
    private String reportId;
    @ApiModelProperty(value = "指标拆解表id")
    @TableField(value = "disassemble_id")
    @ExcelIgnore
    private String disassembleId;
    @ApiModelProperty(value = "地区主键")
    @TableField(value = "region_id")
    @ExcelIgnore
    private Integer regionId;
    @ApiModelProperty(value = "项目")
    @TableField(exist = false)
    @ExcelProperty("项目")
    @ColumnWidth(40)
    private String projectName;
    @ApiModelProperty(value = "重点指标,重点项目名称")
    @TableField("draw_name")
    @ExcelProperty("重点指标,重点项目名称")
    @ColumnWidth(40)
    private String drawName;
    @ApiModelProperty(value = "全年任务")
    @TableField("task")
    @ExcelProperty("全年任务")
    private String task;
    @ApiModelProperty(value = "年份")
    @ExcelIgnore
    @TableField("year")
    private Integer year;
    @ApiModelProperty(value = "当月进度")
    @TableField(exist = false)
    @ExcelProperty("当月进度")
    private String progress;
    @ApiModelProperty(value = "累计完成")
    @TableField(exist = false)
    @ExcelProperty("累计完成")
    private String sum;
    @ApiModelProperty(value = "总进度")
    @TableField(exist = false)
    @ExcelProperty("总进度")
    private String sumProgress;
    @TableField(exist = false)
    @ApiModelProperty(value = "数值类型 0数字 1百分比 2文本")
    @ExcelIgnore
    private Integer numberType;

}

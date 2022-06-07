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
 * 月份数据
 * </p>
 *
 * @author LZK
 * @since 2022-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("month_info")
@ApiModel(value = "MonthInfo对象", description = "月份数据")
public class MonthInfo extends BaseEntity {
    public static final String MONTH_ID = "month_id";
    public static final String BIZ_ID = "biz_id";
    public static final String MONTH = "month";
    public static final String DATA = "data";

    //审核进度  0初始 1待审核 2已提交 3已复核 4通报 5完成
    public static final int EXAMINE_STATUS_ZERO = 0;
    public static final int EXAMINE_STATUS_ONE = 1;
    public static final int EXAMINE_STATUS_TWO = 2;
    public static final int EXAMINE_STATUS_THREE = 3;
    public static final int EXAMINE_STATUS_FOUR = 4;
    public static final int EXAMINE_STATUS_FIVE = 5;


    @ApiModelProperty(value = "月份主键")
    @TableId(value = "month_id", type = IdType.UUID)
    private String monthId;

    @ApiModelProperty(value = "业务主键")
    @TableField("biz_id")
    private String bizId;

    @ApiModelProperty(value = "月份")
    @TableField("month")
    private Integer month;

    @ApiModelProperty(value = "数据")
    @TableField("data")
    private String data;

    @TableField("examine_status")
    @ApiModelProperty(value = "数据审核 0初始 1待审核 2已提交 3已复核 4通报 5完成")
    private Integer examineStatus;

    @TableField("status")
    @ApiModelProperty(value = "审核状态 1成功 2失败")
    private Integer status;

    @TableField("remark")
    @ApiModelProperty(value = "审核意见")
    private String remark;

}

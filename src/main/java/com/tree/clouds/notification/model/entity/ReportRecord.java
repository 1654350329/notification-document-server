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
 *
 * </p>
 *
 * @author LZK
 * @since 2022-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("report_record")
@ApiModel(value = "ReportRecord对象", description = "")
public class ReportRecord extends BaseEntity {

    public static final String RECORD_ID = "record_id";

    public static final String MONTH_ID = "month_id";

    public static final String EXAMINE_STATUS = "examine_status";

    @ApiModelProperty(value = "记录主键")
    @TableId(value = "record_id", type = IdType.UUID)
    private String recordId;

    @ApiModelProperty(value = "月份主键")
    @TableField(value = "month_id")
    private String monthId;

    @TableField("examine_status")
    private String examineStatus;


}

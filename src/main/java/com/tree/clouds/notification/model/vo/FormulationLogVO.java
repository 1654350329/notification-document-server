package com.tree.clouds.notification.model.vo;

import com.tree.clouds.notification.model.entity.FormulationLog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class FormulationLogVO {
    @ApiModelProperty("通报日志数据")
    private FormulationLog formulationLog;
    @ApiModelProperty("各地区数据")
    private List<DataInfoVO> dataInfoVOS;
}

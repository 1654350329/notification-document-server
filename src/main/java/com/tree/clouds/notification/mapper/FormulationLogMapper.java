package com.tree.clouds.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.notification.model.entity.FormulationLog;
import com.tree.clouds.notification.model.vo.FormulationLogPageVO;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-01-11
 */
public interface FormulationLogMapper extends BaseMapper<FormulationLog> {

    IPage<FormulationLog> formulationLogPage(IPage<FormulationLog> page, @Param("formulationLogPageVO") FormulationLogPageVO formulationLogPageVO);
}

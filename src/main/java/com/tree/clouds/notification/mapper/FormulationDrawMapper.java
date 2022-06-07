package com.tree.clouds.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.notification.model.entity.FormulationDraw;
import com.tree.clouds.notification.model.vo.DataInfoVO;
import com.tree.clouds.notification.model.vo.FormulationDrawPageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 指标制定表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
public interface FormulationDrawMapper extends BaseMapper<FormulationDraw> {

    IPage<FormulationDraw> drawPage(IPage<FormulationDraw> page, @Param("formulationDrawPageVO") FormulationDrawPageVO formulationDrawPageVO);

    List<DataInfoVO> dataInfo(@Param("year") int year);

    int distributionCount(@Param("drawId") String drawId);

}

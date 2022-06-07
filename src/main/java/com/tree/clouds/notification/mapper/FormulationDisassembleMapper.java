package com.tree.clouds.notification.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tree.clouds.notification.model.bo.FormulationDisassembleBO;
import com.tree.clouds.notification.model.entity.FormulationDisassemble;
import com.tree.clouds.notification.model.vo.FormulationDrawPageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 指标拆解表 Mapper 接口
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
public interface FormulationDisassembleMapper extends BaseMapper<FormulationDisassemble> {

    IPage<FormulationDisassembleBO> disassemblePage(IPage<FormulationDisassembleBO> page, @Param("formulationDrawPageVO") FormulationDrawPageVO formulationDrawPageVO, @Param("regionId") Integer regionId);

    List<FormulationDisassemble> detailDisassemble(String drawId);
}

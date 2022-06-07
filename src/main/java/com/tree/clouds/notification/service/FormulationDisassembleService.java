package com.tree.clouds.notification.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.notification.model.bo.FormulationDisassembleBO;
import com.tree.clouds.notification.model.entity.FormulationDisassemble;
import com.tree.clouds.notification.model.vo.FormulationDisassembleVO;
import com.tree.clouds.notification.model.vo.FormulationDrawPageVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 指标拆解表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
public interface FormulationDisassembleService extends IService<FormulationDisassemble> {

    IPage<FormulationDisassembleBO> disassemblePage(FormulationDrawPageVO formulationDrawPageVO, Integer regionId);

    List<FormulationDisassemble> detailDisassemble(String drawId);

    void saveDisassemble(List<FormulationDisassembleVO> formulationDisassembles);

    void releaseDisassemble(List<String> ids);

    void initDisassemble(String drawId);

    void exportData(Integer year, Integer regionId, HttpServletResponse response);

    void rebuildDisassemble(String disassembleId);
}

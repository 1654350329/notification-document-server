package com.tree.clouds.notification.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.notification.model.entity.FormulationDraw;
import com.tree.clouds.notification.model.vo.DataInfoVO;
import com.tree.clouds.notification.model.vo.FormulationDrawPageVO;
import com.tree.clouds.notification.model.vo.FormulationDrawVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 指标制定表 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
public interface FormulationDrawService extends IService<FormulationDraw> {

    IPage<FormulationDraw> drawPage(FormulationDrawPageVO formulationDrawPageVO);

    void addDraw(FormulationDrawVO formulationDrawVO);

    void updateDraw(FormulationDrawVO formulationDrawVO);

    void exportDraw(Integer year, HttpServletResponse response);

    void copyDraw(Integer year);

    List<DataInfoVO> dataInfo(Integer year, String drawId);

    void removeDraw(String id);

    FormulationDraw getDrawInfo(String id);

    FormulationDraw getByDisassembleId(String disassembleId);

    int distributionCount(String drawId);

    void updateSort(String drawId, Integer sort);
}

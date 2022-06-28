package com.tree.clouds.notification.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.notification.model.entity.FormulationLog;
import com.tree.clouds.notification.model.vo.FormulationLogPageVO;
import com.tree.clouds.notification.model.vo.FormulationLogVO;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-01-11
 */
public interface FormulationLogService extends IService<FormulationLog> {

    IPage<FormulationLog> formulationLogPage(FormulationLogPageVO formulationLogPageVO);

    FormulationLogVO formulationLogDetail(String logId);

    void exportFormulationLog(String id, HttpServletResponse response);

    void exportLog(String logId, String drawId, HttpServletResponse response);
}

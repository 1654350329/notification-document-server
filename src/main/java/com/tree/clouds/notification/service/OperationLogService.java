package com.tree.clouds.notification.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.notification.model.entity.OperationLog;
import com.tree.clouds.notification.model.vo.OperationLogPageVO;


/**
 * <p>
 * 操作日志 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-01-02
 */
public interface OperationLogService extends IService<OperationLog> {

    IPage<OperationLog> operationLogPage(OperationLogPageVO operationLogPageVO);
}

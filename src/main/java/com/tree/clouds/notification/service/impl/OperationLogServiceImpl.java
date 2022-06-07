package com.tree.clouds.notification.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.notification.mapper.OperationLogMapper;
import com.tree.clouds.notification.model.entity.OperationLog;
import com.tree.clouds.notification.model.vo.OperationLogPageVO;
import com.tree.clouds.notification.service.OperationLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-01-02
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Override
    public IPage<OperationLog> operationLogPage(OperationLogPageVO operationLogPageVO) {
        IPage<OperationLog> page = operationLogPageVO.getPage();
        return this.baseMapper.operationLogPage(page, operationLogPageVO);
    }
}

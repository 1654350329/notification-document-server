package com.tree.clouds.notification.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.clouds.notification.model.entity.LoginLog;
import com.tree.clouds.notification.model.vo.LoginLogPageVO;


/**
 * <p>
 * 登入日志 服务类
 * </p>
 *
 * @author LZK
 * @since 2022-01-02
 */
public interface LoginLogService extends IService<LoginLog> {

    IPage<LoginLog> loginLogPage(LoginLogPageVO loginLogPageVO);

    void updateLongTime(String userId);
}

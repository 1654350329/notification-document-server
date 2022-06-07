package com.tree.clouds.notification.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.notification.mapper.LoginLogMapper;
import com.tree.clouds.notification.model.entity.LoginLog;
import com.tree.clouds.notification.model.vo.LoginLogPageVO;
import com.tree.clouds.notification.service.LoginLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登入日志 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-01-02
 */
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Override
    public IPage<LoginLog> loginLogPage(LoginLogPageVO loginLogPageVO) {
        IPage<LoginLog> page = loginLogPageVO.getPage();
        QueryWrapper<LoginLog> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc(LoginLog.CREATE_TIME);
        if (StrUtil.isNotBlank(loginLogPageVO.getIp())) {
            wrapper.eq(LoginLog.IP, loginLogPageVO.getIp());
        }
        if (StrUtil.isNotBlank(loginLogPageVO.getAccount())) {
            wrapper.eq(LoginLog.ACCOUNT, loginLogPageVO.getAccount());
        }
        if (StrUtil.isNotBlank(loginLogPageVO.getLoginStartTime())) {
            wrapper.gt(LoginLog.CREATE_TIME, loginLogPageVO.getLoginStartTime());
        }
        if (StrUtil.isNotBlank(loginLogPageVO.getLoginEndTime())) {
            wrapper.lt(LoginLog.CREATE_TIME, loginLogPageVO.getLoginEndTime());
        }
        return this.baseMapper.selectPage(page, wrapper);

    }

    @Override
    public void updateLongTime(String userId) {
        try {
            if (userId != null) {
                LoginLog loginLog = this.baseMapper.selectOne(new QueryWrapper<LoginLog>()
                        .eq(LoginLog.CREATE_USER, userId)
                        .orderByDesc(LoginLog.CREATE_TIME)
                        .last("limit 1"));

                loginLog.setLongTime(loginLog.getCreateTime() + " - " + DateUtil.now());
                this.baseMapper.updateById(loginLog);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }


    }
}

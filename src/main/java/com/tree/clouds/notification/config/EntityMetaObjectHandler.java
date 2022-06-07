package com.tree.clouds.notification.config;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.tree.clouds.notification.utils.LoginUserUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * mybatis-plus自动置值
 */
@Component
public class EntityMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("del", 0, metaObject);
        this.setFieldValByName("createUser", LoginUserUtil.getUserId(), metaObject);
        this.setFieldValByName("createTime", DateUtil.now(), metaObject);
        this.setFieldValByName("updateUser", LoginUserUtil.getUserId(), metaObject);
        this.setFieldValByName("updateTime", DateUtil.now(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateUser", LoginUserUtil.getUserId(), metaObject);
        this.setFieldValByName("updateTime", DateUtil.now(), metaObject);
    }
}

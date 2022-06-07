package com.tree.clouds.notification.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tree.clouds.notification.model.entity.RegionUser;

public interface RegionUserMapper extends BaseMapper<RegionUser> {
    default void removeUserByUserId(String userId) {
        this.delete(new QueryWrapper<RegionUser>().eq(RegionUser.USER_ID, userId));
    }
}

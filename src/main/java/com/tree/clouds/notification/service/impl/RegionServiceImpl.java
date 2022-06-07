package com.tree.clouds.notification.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tree.clouds.notification.mapper.RegionMapper;
import com.tree.clouds.notification.model.entity.Region;
import com.tree.clouds.notification.service.RegionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地区表 服务实现类
 * </p>
 *
 * @author LZK
 * @since 2022-01-06
 */
@Service
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements RegionService {

}

package com.onestop.ecosystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onestop.common.service.impl.ServiceWithRedisImpl;
import com.onestop.ecosystem.entity.CommonOrder;
import com.onestop.ecosystem.entity.ThematicType;
import com.onestop.ecosystem.mapper.CommonOrderMapper;
import com.onestop.ecosystem.mapper.ThematicTypeMapper;
import com.onestop.ecosystem.service.ICommonOrderService;
import com.onestop.ecosystem.service.IThematicTypeService;
import org.springframework.stereotype.Service;

@Service("thematicTypeService")
public class ThematicTypeServiceImpl extends ServiceImpl<ThematicTypeMapper, ThematicType> implements IThematicTypeService {

}

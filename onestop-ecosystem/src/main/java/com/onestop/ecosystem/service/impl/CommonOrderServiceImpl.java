package com.onestop.ecosystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.onestop.common.service.impl.ServiceWithRedisImpl;
import com.onestop.ecosystem.mapper.CommonOrderMapper;
import com.onestop.ecosystem.entity.CommonOrder;
import com.onestop.ecosystem.service.ICommonOrderService;
import org.springframework.stereotype.Service;

@Service("commonOrderService")
public class CommonOrderServiceImpl extends ServiceWithRedisImpl<CommonOrderMapper, CommonOrder> implements ICommonOrderService {

    @Override
    public CommonOrder getByOrderId(String orderId) {
        LambdaQueryWrapper<CommonOrder> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CommonOrder::getOrderId, orderId);
        return this.getOne(lambdaQueryWrapper);
    }
}

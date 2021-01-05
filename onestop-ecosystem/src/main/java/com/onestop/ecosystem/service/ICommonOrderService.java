package com.onestop.ecosystem.service;

import com.onestop.ecosystem.entity.CommonOrder;
import com.baomidou.mybatisplus.extension.service.IService;
public interface ICommonOrderService extends IService<CommonOrder>{
    CommonOrder getByOrderId(String orderId);
}

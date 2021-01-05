package com.onestop.ecosystem.util;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 用于存储订单执行状态
 * @author: chenq
 * @date: 2019/9/9 14:41
 */
@Data
public class RedisObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer status;
    private String productId;
    private String reason;
}

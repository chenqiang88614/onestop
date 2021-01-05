package com.onestop.ecosystem.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description: ProdOrder的前端显示
 * @author: chenq
 * @date: 2019/9/5 14:19
 */
@Data
public class ProdOrderVo {
    private String id;
    private String orderId;
    private String urgencyLevel;
    private String userName;
    private String mark;
    private String satelliteId;
    private String thematicType;
    private String productId;
    private String status;
    private Date createTime;
}

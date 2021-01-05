package com.onestop.ecosystem.bean;

import lombok.Data;

/**
 * @description: 专题产品定制单确认
 * @author: chenq
 * @date: 2019/9/6 11:13
 */
@Data
public class ProdOrderAck {
    private String orginalId;
    private String orderId;
    private String operatorName;
    private String satelliteId;
    private String status;
    private String reason;
}

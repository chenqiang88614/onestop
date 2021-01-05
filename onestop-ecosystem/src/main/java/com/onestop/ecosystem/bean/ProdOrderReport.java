package com.onestop.ecosystem.bean;

import lombok.Data;

/**
 * @description: 专题产品定制完成报告
 * @author: chenq
 * @date: 2019/9/9 11:24
 */
@Data
public class ProdOrderReport {
    private String orderId;
    private String operatorName;
    private String satelliteId;
    private String status;
    private String reason;
}

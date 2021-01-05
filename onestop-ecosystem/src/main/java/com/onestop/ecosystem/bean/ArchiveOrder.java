package com.onestop.ecosystem.bean;

import lombok.Data;

/**
 * @description: 数据产品归档订单
 * @author: chenq
 * @date: 2019/9/9 10:11
 */
@Data
public class ArchiveOrder {
    private String operatorName;
    private String archiveType;
    private String satelliteId;
    private String orderId;
    private String taskId;
    private String productLevel;
    private String productId;
    private String pixelSpacing;
    private String fileName;
}

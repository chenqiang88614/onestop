package com.onestop.ecosystem.bean;

import lombok.Data;

/**
 * @description: 数据提取订单
 * @author: chenq
 * @date: 2019/9/5 13:17
 */
@Data
public class ExtractOrder {
    private String operatorName;
    private String orderId;
    private String satelliteId;
    private String sensorId;
    private String stationId;
    private String bandsOrdered;
    private String productId;
    private String sceneId;
    private String orbitId;
    private String scenePath;
    private String sceneRow;
    private String productType;
}

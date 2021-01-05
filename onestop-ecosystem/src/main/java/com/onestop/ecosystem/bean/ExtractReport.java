package com.onestop.ecosystem.bean;

import lombok.Data;

/**
 * @description: 数据提取完成报告
 * @author: chenq
 * @date: 2019/9/6 16:29
 */
@Data
public class ExtractReport {
    private String orderId;
    private String satelliteId;
    private String status;
    private String reason;
    private String dataPath;
    private String productId;
    private String senceId;
}

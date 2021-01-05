package com.onestop.ecosystem.bean;

import lombok.Data;

/**
 * @description: 专题产品归档确认
 * @author: chenq
 * @date: 2019/9/9 11:23
 */
@Data
public class ArchiveReport {
    private String orderId;
    private String taskId;
    private String operatorName;
    private String satelliteId;
    private String status;
    private String reason;
}

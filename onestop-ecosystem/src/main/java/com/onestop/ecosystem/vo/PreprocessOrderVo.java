package com.onestop.ecosystem.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description: 预处理订单VO
 * @author: chenq
 * @date: 2019/9/6 15:39
 */
@Data
public class PreprocessOrderVo {
    private String id;
    private String taskId;
    private String orderId;
    private String dataPath;
    private String dataName;
    private String thematicType;
    private String thematicName;
    private String status;
    private String satelliteId;
    private Date creationTime;
    private String productId;
    private String reason;
}

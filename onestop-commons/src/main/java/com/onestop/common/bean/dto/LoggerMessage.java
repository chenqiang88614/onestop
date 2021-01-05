package com.onestop.common.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @description: 日志实体类
 * @author: chenq
 * @date: 2019/10/14 14:51
 */
@Data
@AllArgsConstructor
public class LoggerMessage {
    private String body;
    private String timestamp;
    private String threadName;
    private String className;
    private String level;
    private String exception;
    private String cause;
}

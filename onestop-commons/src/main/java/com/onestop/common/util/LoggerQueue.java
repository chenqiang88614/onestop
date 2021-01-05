package com.onestop.common.util;

import com.onestop.common.bean.dto.LoggerMessage;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @description: 日志的临时载体消息队列
 * @author: chenq
 * @date: 2019/10/14 14:54
 */
public class LoggerQueue {
    public static final int QUEUE_MAX_SIZE = 10000;
    private static LoggerQueue alarmMessageQueue = new LoggerQueue();

    /**阻塞队列*/
    private BlockingDeque blockingDeque = new LinkedBlockingDeque(QUEUE_MAX_SIZE);

    private LoggerQueue() {

    }

    public static LoggerQueue getInstance() {
        return alarmMessageQueue;
    }

    /**
     * @description 消息入队
     * @param log
     * @return
     */
    public boolean push(LoggerMessage log) {
        return this.blockingDeque.add(log);
    }

    /**
     * @description 消息出队
     * @return
     */
    public LoggerMessage poll() {
        LoggerMessage result = null;
        try {
            result = (LoggerMessage) this.blockingDeque.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}

package com.onestop.ecosystem.service.impl;

import com.onestop.ecosystem.service.IPreprocessOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @description: messageLister
 * @author: chenq
 * @date: 2019/9/10 8:37
 */
@Slf4j
public class ConsumerRedisListener implements MessageListener {
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private IPreprocessOrderService preprocessOrderService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        threadPoolTaskExecutor.execute(() -> preprocessOrderService.consumMessage(message, pattern));

    }
}

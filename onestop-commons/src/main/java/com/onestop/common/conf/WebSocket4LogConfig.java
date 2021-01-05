package com.onestop.common.conf;

import com.onestop.common.bean.dto.LoggerMessage;
import com.onestop.common.util.LoggerQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 这个类表示启用websocket消息处理，以及收发消息的域
 */
@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocket4LogConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private SimpMessagingTemplate messagingTemplate;


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
         /*
          * 路径"/webSocketEndPoint"被注册为STOMP端点，对外暴露，客户端通过该路径接入WebSocket服务
          */
        registry.addEndpoint("/webSocket").setAllowedOrigins("*").setHandshakeHandler(new DefaultHandshakeHandler()).withSockJS();
    }


    @PostConstruct
    public void pushLogger() {
        threadPoolTaskExecutor.submit(() -> {
           while (true) {
               try {
                   LoggerMessage loggerMessage = LoggerQueue.getInstance().poll();
                   if (loggerMessage != null) {
                       if (messagingTemplate != null) {
                           messagingTemplate.convertAndSend("/topic/pullLogger", loggerMessage);
                       }
                   }
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
        });
    }
}

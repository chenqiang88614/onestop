package com.onestop.websocket.websocket;

import com.onestop.common.bean.dto.LoggerMessage;
import com.onestop.common.util.LoggerQueue;
import com.onestop.websocket.login.Authentication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ImmutableMessageChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 这个类表示启用websocket消息处理，以及收发消息的域
 */
//@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        /*
         * 用户可以订阅来自"/topic"和"/user"的消息，
         * 在Controller中，可通过@SendTo注解指明发送目标，这样服务器就可以将消息发送到订阅相关消息的客户端
         *
         * 在本Demo中，使用topic来达到群发效果，使用user进行一对一发送
         *
         * 客户端只可以订阅这两个前缀的主题
         */
        config.enableSimpleBroker("/topic", "/user");
        /*
         * 客户端发送过来的消息，需要以"/app"为前缀，再经过Broker转发给响应的Controller
         */
        config.setApplicationDestinationPrefixes("/app");

        /*
         * 一对一发送的前缀
         * 订阅主题：/user/{userID}//demo3/greetings
         * 推送方式：1、@SendToUser("/demo3/greetings")
         *          2、messagingTemplate.convertAndSendToUser(destUsername, "/demo3/greetings", greeting);
         */
        config.setUserDestinationPrefix("/user");
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
         /*
          * 路径"/webSocketEndPoint"被注册为STOMP端点，对外暴露，客户端通过该路径接入WebSocket服务
          */
        registry.addEndpoint("/webSocketEndPoint").setAllowedOrigins("*").withSockJS();
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ImmutableMessageChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                //1. 判断是否首次连接请求
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {

                    //2. 验证是否登录
                    String username = accessor.getNativeHeader("username").get(0);
                    if (StringUtils.isEmpty(username)) {
                        return null;
                    }
                    log.info("CONNECT user = {}, sessionId = {}", username, accessor.getSessionId());

                    redisTemplate.opsForSet().add("PUSH:" + username, accessor.getSessionId());

                    Authentication user = new Authentication(username); // access authentication header(s)}
                    accessor.setUser(user);
                    return message;

                   /* for (Map.Entry<String, String> entry : Users.USERS_MAP.entrySet()) {
//                        System.out.println(entry.getKey() + "---" + entry.getValue());
                        if (entry.getKey().equals(username) && entry.getValue().equals(password)) {
                            //验证成功,登录
                            Authentication user = new Authentication(username); // access authentication header(s)}
                            accessor.setUser(user);
                            return message;
                        }
                    }
                    return null;*/
                } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {

                    String username = ((Authentication)accessor.getHeader("simpUser")).getName();
                    log.info("DISCONNECT user = {}, sessionId = {}", username, accessor.getSessionId());
                    redisTemplate.opsForSet().remove("PUSH:" + username, accessor.getSessionId());
                    Long size = redisTemplate.opsForSet().size("PUSH:" + username);
                    if (size == 0) {
                        redisTemplate.opsForValue().set("PUSH:" + username + ":MONITOR", 0);
                        log.info("当前用户 {} 所有登录均已下线，关闭实时数据推送。", username);
                    }
                }

                //不是首次连接，已经成功登陆
                return message;
            }
        });
    }

}

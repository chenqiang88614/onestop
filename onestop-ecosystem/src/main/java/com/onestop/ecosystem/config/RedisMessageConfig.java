package com.onestop.ecosystem.config;

import com.onestop.ecosystem.service.impl.ConsumerRedisListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @description: redis做消息队列配置
 * @author: chenq
 * @date: 2019/9/10 8:36
 */
@Configuration
public class RedisMessageConfig {
    @Bean

    public MessageListener consumerRedis() {

        return new ConsumerRedisListener();

    }

    @Bean

    public ChannelTopic topic() {

        return new ChannelTopic("BIZ:PUB:PROCESS:RESULT");

    }


    @Bean

    public RedisMessageListenerContainer redisMessageListenerContainer(@Qualifier("redisConnectionFactory") RedisConnectionFactory factory) {


        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        container.setConnectionFactory(factory);


        container.addMessageListener(consumerRedis(), topic());


        return container;


    }

}

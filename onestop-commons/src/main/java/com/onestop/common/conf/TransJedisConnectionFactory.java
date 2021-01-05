package com.onestop.common.conf;

import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

public class TransJedisConnectionFactory extends JedisConnectionFactory {

    public TransJedisConnectionFactory(JedisPoolConfig poolConfig) {
        super((RedisSentinelConfiguration) null, poolConfig);
    }
}

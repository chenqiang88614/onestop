package com.onestop.ecosystem.service.impl;

import com.onestop.ecosystem.service.ISequenceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @description 使用Redis生成分布式自增ID
 * @author chenq
 * @date 2019年9月2日15:34:51
 */
@Service("sequenceFactory")
public class SequenceFactoryImpl implements ISequenceFactory {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public void set(String key, int value, Date expireTime) {
        RedisAtomicLong atomicLong = new RedisAtomicLong(key, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        atomicLong.set(value);
        atomicLong.expireAt(expireTime);
    }

    @Override
    public void set(String key, int value, long timeout, TimeUnit unit) {
        RedisAtomicLong atomicLong = new RedisAtomicLong(key, Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        atomicLong.set(value);
        atomicLong.expire(timeout, unit);
    }

    @Override
    public long generate(String key, Date expireTime) {
        RedisAtomicLong atomicLong = new RedisAtomicLong(key,
                Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        atomicLong.expireAt(expireTime);
        return atomicLong.incrementAndGet();
    }

    @Override
    public long generate(String key) {
        RedisAtomicLong atomicLong = new RedisAtomicLong(key,
                Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        return atomicLong.incrementAndGet();
    }

    @Override
    public long generate(String key, int increment) {
        RedisAtomicLong atomicLong = new RedisAtomicLong(key,
                Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        return atomicLong.addAndGet(increment);
    }

    @Override
    public long generate(String key, int increment, Date expireTime) {
        RedisAtomicLong atomicLong = new RedisAtomicLong(key,
                Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        atomicLong.expireAt(expireTime);
        return atomicLong.addAndGet(increment);
    }
}

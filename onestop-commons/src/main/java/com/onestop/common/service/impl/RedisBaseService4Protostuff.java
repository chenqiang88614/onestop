package com.onestop.common.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

/**
 * Protostuff序列化方式操作Redis, 仅用于与表对象缓存和分页缓存
 * 如需操作Redis进行setNx / incr / decr 等, 请优先使用另外一种序列化方式的 RedisBaseService
 */
@Service
public class RedisBaseService4Protostuff {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisTemplate<String, Object> redisTemplate4Protostuff;

    @Autowired
    private RedisTemplate<String, Object> transRedisTemplate4Protostuff;

    //=============================common============================
    public void expire(String key, long second) {
        expire(key, second, false);
    }

    public void expire(String key, long second, boolean isUseTran) {
        if (second > 0) {
            RedisTemplate<String, Object> template = getRedisTemplate(isUseTran);
            template.expire(key, second, TimeUnit.SECONDS);
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate4Protostuff.delete(key[0]);
            } else {
                redisTemplate4Protostuff.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String key, boolean isUseTran) {
        getRedisTemplate(isUseTran).delete(key);
    }

    //============================String=============================
    public Object get(String key) {
        return get(key, false);
    }

    public Object get(String key, boolean isUseTran) {
        if (key != null) {
            RedisTemplate<String, Object> template = getRedisTemplate(isUseTran);
            return template.opsForValue().get(key);
        }
        return null;
    }

    public boolean set(String key, Object value, boolean isUseTran) {
        RedisTemplate<String, Object> template = getRedisTemplate(isUseTran);
        template.opsForValue().set(key, value);
        return true;
    }

    public boolean set(String key, Object value, long time, boolean isUseTran) {
        RedisTemplate<String, Object> template = getRedisTemplate(isUseTran);
        if (time > 0) {
            template.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            template.opsForValue().set(key, value);
        }
        return true;
    }

    private RedisTemplate<String, Object> getRedisTemplate(boolean isUseTran) {
//        return isUseTran ? transRedisTemplate4Protostuff : redisTemplate4Protostuff;
        return redisTemplate4Protostuff;
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate4Protostuff.opsForHash().get(key, item);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time, boolean isUseTran) {
        getRedisTemplate(isUseTran).opsForHash().put(key, item, value);
        if (time > 0) {
            expire(key, time);
        }
        return true;
    }

}

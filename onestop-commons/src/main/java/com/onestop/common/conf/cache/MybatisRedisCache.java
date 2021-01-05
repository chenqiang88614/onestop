package com.onestop.common.conf.cache;

import com.onestop.common.util.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 使用Redis来做Mybatis的二级缓存
 * 实现Mybatis的Cache接口
 */
//@Lazy(false)
//@Component
public class MybatisRedisCache implements Cache {

    private static final Logger logger = LoggerFactory.getLogger(MybatisRedisCache.class);

    private static final String KEY_PRIX = "CACHE";

    // 读写锁
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    private RedisTemplate<String, Object> redisTemplate;

    private String id;

    public MybatisRedisCache(){}

    public MybatisRedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        logger.info("Redis Cache id " + id);
        this.id = id;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        if (value != null) {
            // 向Redis中添加数据，有效时间是1小时
            redisTemplate = getRedisTemplate();
            if (!StringUtils.contains(key.toString(), "Page")) {
                redisTemplate.opsForValue().set(KEY_PRIX + ":" + key.toString(), value, 1, TimeUnit.HOURS);
            }
        }
    }

    @Override
    public Object getObject(Object key) {
        try {
            if (key != null) {
                redisTemplate = getRedisTemplate();
                if (!StringUtils.contains(key.toString(), "Page")) {
                    Object obj = redisTemplate.opsForValue().get(KEY_PRIX + ":" + key.toString());
                    logger.info(obj.toString());
                    return obj;
                }
            }
        } catch (Exception e) {
            if (!StringUtils.contains(key.toString(), "Page")) {
                logger.warn("redis ");
            }
        }
        return null;
    }

    @Override
    public Object removeObject(Object key) {
        try {
            if (key != null) {
                redisTemplate = getRedisTemplate();
                redisTemplate.delete(KEY_PRIX + ":" + key.toString());
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public void clear() {
        logger.debug("清空缓存");
        try {
            redisTemplate = getRedisTemplate();
            Set<String> keys = redisTemplate.keys(KEY_PRIX + ":" + "*:" + this.id + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                redisTemplate.delete(keys);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public int getSize() {
        redisTemplate = getRedisTemplate();
        Long size = (Long) redisTemplate.execute((RedisCallback<Long>) connection -> connection.dbSize());
        return size.intValue();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }

    private RedisTemplate<String, Object> getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = SpringContextHolder.getBean("redisTemplate");
        }
        return redisTemplate;
    }
}

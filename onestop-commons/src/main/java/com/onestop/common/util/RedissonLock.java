package com.onestop.common.util;

import com.onestop.common.conf.RedissonManager;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

//@Component
@Slf4j
public class RedissonLock {
    private static RedissonClient redissonClient = RedissonManager.getRedissonClient();

    /**
     * @description 加锁
     * @param lockName redis中的key
     * @param timeout 超时强制解锁
     */
    public void lock(String lockName, long timeout) {
        String key = lockName;
        RLock myLock = redissonClient.getLock(key);
        myLock.lock(timeout, TimeUnit.SECONDS);
        /**
        尝试加锁，最多等待3秒，上锁以后10秒自动解锁
         try {
             boolean res = myLock.tryLock(3, 10, TimeUnit.SECONDS);
         } catch (InterruptedException e) {
             e.printStackTrace()
         }
         */
    }

    /**
     * @description 解锁
     * @param lockName redis中的key
     */
    public void unLock(String lockName) {
        String key = lockName;
        RLock myLock = redissonClient.getLock(key);
        myLock.unlock();
    }
}

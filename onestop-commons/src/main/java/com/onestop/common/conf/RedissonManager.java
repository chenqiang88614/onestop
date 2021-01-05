package com.onestop.common.conf;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.IntStream;

/**
 * @author Administrator
 * @Describe 用于redis分布式锁的相关配置
 */
//@Component
@Slf4j
public class RedissonManager {
    private static Config config= new Config();
    private static RedissonClient redissonClient = null;
    public static final String RAtomicName = "genId_";

    @Value("${spring.redis.cluster.nodes}")
    private String nodes;

    private static String[] clusterNodes;

    @PostConstruct
    public void setClusterNode() {
        String[] nodeSplit = StringUtils.split(nodes, ",");
        int length = nodeSplit.length;
        clusterNodes = new String[length];
        IntStream.range(0, length).forEach(i -> clusterNodes[i] = "redis://" + nodeSplit[i]);
    }

    /**
     * @description Redisson 3.2.0中的clusterNodes不支持redis://，如果更新到高版本的Redisson，采用redis://的方式
     */
    public static void init() {
        try {
            config.useClusterServers()
                    // 设置集群状态扫描间隔
                    .setScanInterval(200000)
                    // 设置对于master节点的连接池中连接数最大为10000
                    .setMasterConnectionPoolSize(10000)
                    // 设置对于slave节点的连接池中连接数最大为10000
                    .setSlaveConnectionPoolSize(10000)
                    // 如果当前连接池里的连接数超过了最小空闲连接数，二同时有连接空闲时间超过了该数值，那么这些连接将会自动被关闭，并从连接池里取消，时间单位是毫秒
                    .setIdleConnectionTimeout(10000)
                    // 同任何节点建立连接时的等待超时，时间单位是毫秒
                    .setConnectTimeout(3000)
                    // 等待节点回复命令的时间，改时间从命令发送成功时开始计时，
                    .setTimeout(3000)
                    // 当与某个节点的连接断开时，等待与其重新建立时间的时间间隔，时间单位是毫秒
                    .setRetryInterval(3000);
//                    .addNodeAddress("172.23.1.71:7001","172.23.1.71:7002","172.23.1.72:7001","172.23.1.72:7002","172.23.1.73:7001","172.23.1.73:7002");

            redissonClient = Redisson.create(config);

            RAtomicLong atomicLong = redissonClient.getAtomicLong(RAtomicName);
            atomicLong.set(0);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("RedissonManager 初始化错误，原因：{}", e.getLocalizedMessage());
        }
    }

    public static RedissonClient getRedissonClient() {
        if (redissonClient == null) {
            RedissonManager.init();
        }
        return redissonClient;
    }
}

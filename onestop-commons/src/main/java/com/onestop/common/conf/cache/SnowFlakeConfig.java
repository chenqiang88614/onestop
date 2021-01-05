package com.onestop.common.conf.cache;

import com.onestop.common.util.SnowflakeIdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 雪花算法
 * @author: chenq
 * @date: 2019/9/9 17:30
 */
@Configuration
public class SnowFlakeConfig {

    @Bean
    public SnowflakeIdWorker snowflakeIdWorker() {
        return new SnowflakeIdWorker(0, 0);
    }
}

package com.onestop.ecosystem.util;

import com.onestop.common.util.SnowflakeIdWorker;

/**
 * @description: 雪花算法id生成器
 * @author: chenq
 * @date: 2019/9/9 16:34
 */

public enum SnowFlakeUtil {
    INSTANCE;

    SnowFlakeUtil() {
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
    }


}

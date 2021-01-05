package com.onestop.common.enums;

public interface SystemConst {

    /**
     * 默认当前页为1
     */
    Integer CURRENT_PAGE_NUM          = 1;

    /**
     * 默认一页10行
     */
    Integer PAGE_SIZE             = 10;

    String PG_REDIS_PRE             = "PG2REDIS:";

    String DEFAULT_START_TIME       = "2000-01-01 00:00:00";

    String DEFAULT_END_TIME         = "2049-12-31 23:59:59";
}

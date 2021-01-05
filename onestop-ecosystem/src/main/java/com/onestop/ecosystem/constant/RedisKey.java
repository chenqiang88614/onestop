package com.onestop.ecosystem.constant;

public enum  RedisKey {
    MESSAGE_ID("UUID", "String"),

    MODEL_TO_DB("BASIC:HASH:MODEL2DB", "HASH"),

    MODEL_TO_DB_PREX("BASIC:HASH:", "PREX"),

    COUNTRY_CODE_TO_REDIS_PREX("BIZ:HASH:COUNTRY:", "PREX"),

    ECOLOGICAL_INDEX("BIZ:HASH:ECOLOGICAL_INDEX", "HASH"),

    XML_TO_DB_PREX("BASIC:HASH:XML2DB:", "PREX"),

    STATUS_PREX("BASIC:HASH:STATUS:", "PREX"),

    PROD_ORDER_STATUS("BASIC:HASH:STATUS:PROD_ORDER_STATUS", "HASH"),

    PRE_PROCESS_ORDER_STATUS("BASIC:HASH:STATUS:PRE_PROCESS_ORDER_STATUS", "HASH"),

    THEMATIC_TYPE("BASIC:HASH:STATUS:THEMATIC_TYPE", "HASH"),

    SUB_ORDER_COUNT_PREX("BIZ:HASH:PRODORDER:COUNT", "HASH"),

    SUB_ORDER_RESULT_PREX("BIZ:HASH:PRODORDER:RESULT:", "HASH");

    private String key;
    private String type;

    RedisKey(String key, String type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

package com.onestop.ecosystem.constant;

public enum PreprocessStatus {
    EXTRACT_DATA(0, "正在获取数据"),

    EXTRACT_DATA_FIN(1, "已获取数据"),

//    PRE_PROCESS("正在预处理"),

//    PRE_PROCESS_FIN("预处理成功"),

    PROCESS(2, "正在处理"),

    PROCESS_FIN(3, "处理完成"),

    ARCHIVE_WAIT(4, "等待归档"),

    ARCHIVE(5, "正在归档"),

    ARCHIVE_SUCCESS(6, "归档成功"),

    EXTRACT_DATA_FAIL(7, "获取数据失败"),

//    PRE_PROCESS_FAIL("预处理失败"),

    PROCESS_FAIL(8, "处理失败"),

    ARCHIVE_FAIL(9, "归档失败")

    ;

    private Integer key;

    private String name;

    PreprocessStatus(Integer key, String name) {
        this.key = key;
        this.name = name;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


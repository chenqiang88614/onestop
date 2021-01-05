package com.onestop.ecosystem.constant;

import io.swagger.annotations.Api;

@Api
public enum ProdOrderStatus {
    /**
     * 未执行
     */
    NO_PROCESS(0, "未执行"),

    /**
     * 正在执行
     */
    PROCESSING(1, "正在执行"),

    /**
     * 执行成功
     */
    PROCESS_SUCCESS(2, "执行成功"),

    /**
     * 已完成
     */
    COMPLETE(4, "已完成"),

    /**
     * 处理失败
     */
    PROCESS_FAIL(7, "处理失败"),

    /**
     * 已取消
     */
    CANCEL(8, "已取消"),

    REJECT(9, "拒绝");
    private Integer key;
    private String desc;

    ProdOrderStatus(java.lang.Integer key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public java.lang.Integer getKey() {
        return key;
    }

    public void setKey(java.lang.Integer key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

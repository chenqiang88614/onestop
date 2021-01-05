package com.onestop.common.enums;

/**
 * @author chenq
 * @description
 * @date 2019/6/5 9:41
 */
public enum ProtocolEnum {
    HJ212(0, 0, "环保212协议", "2017版");

    private Integer type;

    private Integer version;

    private String typeStr;

    private String versionStr;

    ProtocolEnum(final Integer type, final Integer version, final String typeStr, final String versionStr) {
        this.type = type;
        this.version = version;
    }

    public Integer getType() {
        return type;
    }

    public Integer getVersion() {
        return version;
    }

    public String getTypeStr() {
        return typeStr;
    }

    public String getVersionStr() {
        return versionStr;
    }}

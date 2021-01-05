package com.onestop.ecosystem.constant;

public enum SystemName {
    PSS("分发系统", "PSS"),
    TAS("本系统", "TAS"),
    DMS("数据管理系统", "DMS");

    private String name;
    private String alias;

    SystemName(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}

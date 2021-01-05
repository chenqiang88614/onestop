package com.onestop.ecosystem.constant;

public enum FileMessageType {
    PROD_ORDER_ACK("PRODORDERACK"),

    EXTRACT_ORDER("EXTRACTORDER"),

    ARCHIVE_ORDER("ARCHIVEORDER"),

    PROD_ORDER_REPORT("PRODORDERREPORT");

    private String type;
    FileMessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

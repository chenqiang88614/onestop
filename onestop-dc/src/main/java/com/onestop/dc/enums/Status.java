package com.onestop.dc.enums;

public enum Status {
    SUCCESS(0, "success"),

    REQUEST_PARAMS_NOT_VALID_ERROR(10001, "request parameter {0} is not valid"),



    ;

    private int code;
    private String msg;

    private Status(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

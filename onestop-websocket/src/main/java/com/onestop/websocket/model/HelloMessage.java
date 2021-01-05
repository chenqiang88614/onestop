package com.onestop.websocket.model;


import lombok.Data;

/**
 * Client --> Server
 */
@Data
public class HelloMessage {
    private String message;

    private String device;

//    public HelloMessage() {
//    }
//
//    public HelloMessage(String message) {
//        this.message = message;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
}

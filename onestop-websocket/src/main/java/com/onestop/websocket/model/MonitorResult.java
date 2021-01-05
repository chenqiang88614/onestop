package com.onestop.websocket.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class MonitorResult {
    private String deviceNumber;

    private List<Map> result;
}

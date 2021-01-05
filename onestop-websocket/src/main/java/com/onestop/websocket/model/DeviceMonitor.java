package com.onestop.websocket.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DeviceMonitor implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 监控间隔时间，单位秒
     */
    private String intervalTime;

    /**
     * 需要监控的设备列表，每一条包括deviceNumber和监控因子，一各设备之间用‘###’区分，每个因子之间用“，”分割
     *
     */
    private List<String> deviceList;
}

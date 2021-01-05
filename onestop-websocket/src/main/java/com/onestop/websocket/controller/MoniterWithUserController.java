package com.onestop.websocket.controller;

import com.onestop.websocket.login.Authentication;
import com.onestop.websocket.model.DeviceMonitor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 点对点推送数据
 * Author: chenq
 * Date: 2019-8-1 13:18:28
 */
@Controller
public class MoniterWithUserController {

    private final SimpMessagingTemplate messagingTemplate;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /*
     * 实例化Controller的时候，注入SimpMessagingTemplate
     */
    @Autowired
    public MoniterWithUserController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/monitor/user/{destUsername}")
    @SendToUser("/monitor/greetings")
    public List<Map> greeting(@DestinationVariable String destUsername, DeviceMonitor deviceMonitor, StompHeaderAccessor headerAccessor) throws Exception {

        Authentication user = (Authentication) headerAccessor.getUser();
        Object object = redisTemplate.opsForValue().get("PUSH:" + user.getName() + ":MONITOR");
        if (object != null && (Integer)object == 1) {
            return new ArrayList<>();
        }
        Integer intervalTime = Integer.parseInt(deviceMonitor.getIntervalTime());
        if (intervalTime == null || intervalTime < 0) {
            intervalTime = 1;
        }
        intervalTime = intervalTime * 1000;

        List<String> deviceList = deviceMonitor.getDeviceList();
        Map<String, List> keyMap = new HashMap<>(16);
        String keyPrex = "DATA:REALTIME:DEVICE:";

        deviceList.stream().map(device -> StringUtils.split(device, "###")).forEach(split -> {
            String key = keyPrex + split[0] + ":FACTOR:";
            String[] factors = StringUtils.split(split[1], ",");
            List<String> deviceMonitorKey = Arrays.stream(factors).map(factor -> key + factor).collect(Collectors.toCollection(() -> new ArrayList<>(16)));
            keyMap.put(split[0], deviceMonitorKey);
        });

        sendMessage(user.getName(), keyMap, intervalTime);
        return new ArrayList<>();
    }

    private void sendMessage(String username, Map<String, List> keyMap, Integer intervalTime) {
        redisTemplate.opsForValue().set("PUSH:" + username + ":MONITOR", 1);
        threadPoolTaskExecutor.execute(() -> {
            do {
                Integer number = (Integer) redisTemplate.opsForValue().get("PUSH:" + username + ":MONITOR");
                if (number == null || number == 0) {
                    break;
                }

                List<Map> message = new ArrayList<>(16);
                keyMap.forEach((k, v) -> {
                    Map<String, Object> deviceMessage = new HashMap<>(16);
                    deviceMessage.put("deviceNumber", k);
                    v.forEach(element -> {
                        Map entries = redisTemplate.opsForHash().entries((String)element);
                        deviceMessage.put(((String)element).substring(53), entries);
                    });
                    message.add(deviceMessage);
                });

                messagingTemplate.convertAndSendToUser(username, "/monitor/greetings", message);
                try {
                    Thread.sleep(intervalTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        });
    }
}

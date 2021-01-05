package com.onestop.websocket.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jws.WebParam;

@RestController(value = "push")
@Api
public class PushController {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping(value = "check")
    @ApiImplicitParam(name = "username", value = "username", paramType = "query")
    @ResponseBody public Integer checkOnMonitor(@WebParam String username) {
        Integer number = (Integer) redisTemplate.opsForValue().get("PUSH:" + username +":MONITOR");
        return number;
    }

    @GetMapping(value = "stop-monitor")
    @ApiImplicitParam(name = "username", value = "username", paramType = "query")
    @ResponseBody public Integer stopMonitor(@WebParam String username) {
        redisTemplate.opsForValue().set("PUSH:" + username +":MONITOR", 0);
        return 0;
    }
}

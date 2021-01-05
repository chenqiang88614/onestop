package com.onestop.ecosystem.controller;

import com.onestop.common.util.SnowflakeIdWorker;
import com.onestop.ecosystem.service.EcologicalIndexService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.jws.WebParam;
import java.util.*;

/**
 * @description: 测试用例
 * @author: chenq
 * @date: 2019/9/6 10:12
 */
@RestController("test")
@Api
@Slf4j
public class TestController {
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private EcologicalIndexService ecologicalIndexService;

    @GetMapping("xml2db")
    public int xml2db() {
        log.info("test");
        return 0;
    }

    @RequestMapping(value = "sendRedis", method = RequestMethod.POST)
    @ApiImplicitParams({@ApiImplicitParam(name = "result", value = "结果，成功0，失败1", paramType = "query"),
            @ApiImplicitParam(name = "reason", value = "失败填写原因", paramType = "query")})
    public int senRedis(@WebParam String result, @WebParam String reason) {
        Map<String, String> map = new HashMap<>();
        map.put("id", "620997313719959552");
        map.put("result", result);
        map.put("reason", reason);
        map.put("dataPath", "C:\\xx.123");
//        redisTemplate.convertAndSend("BIZ:PUB:PROCESS:RESULT", "123");
        redisTemplate.convertAndSend("BIZ:PUB:PROCESS:RESULT", map);
        return 1;
    }


    @GetMapping("deleteCache")
    public int deleteCache() {
        Set keys = redisTemplate.keys("CACHE*");
        keys.addAll(redisTemplate.keys("BiologicalAbundance:*"));
        keys.addAll(redisTemplate.keys("EcologicalIndex:*"));

        redisTemplate.delete(keys);

        return 0;
    }

    @GetMapping("staticts")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "countries", value = "countries", paramType = "query"),
            @ApiImplicitParam(name = "year", value = "year", paramType = "query")
    })
    public int statics(@WebParam String countries, @WebParam String year) {
        ecologicalIndexService.statistic(countries, year);
        return 0;
    }
}

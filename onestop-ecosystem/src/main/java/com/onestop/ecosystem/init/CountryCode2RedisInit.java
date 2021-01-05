package com.onestop.ecosystem.init;

import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.entity.CountryCode;
import com.onestop.ecosystem.service.CountryCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 将县区代码缓存到Redis中
 * @author: chenq
 * @date: 2019/9/27 13:16
 */
@Component
@Slf4j
public class CountryCode2RedisInit implements CommandLineRunner {
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private CountryCodeService countryCodeService;

    @Override
    public void run(String... args) throws Exception {
        List<CountryCode> countryCodes = countryCodeService.list();
        Map<String, String> country2code = new HashMap<>(64);
        Map<String, String> code2country = new HashMap<>(64);
        countryCodes.forEach(countryCode -> {
            country2code.put(countryCode.getCountry(), countryCode.getCode());
            code2country.put(countryCode.getCode(), countryCode.getCountry());
        });
        redisTemplate.opsForHash().putAll(RedisKey.COUNTRY_CODE_TO_REDIS_PREX.getKey() + "1", country2code);
        redisTemplate.opsForHash().putAll(RedisKey.COUNTRY_CODE_TO_REDIS_PREX.getKey() + "2", code2country);
    }
}

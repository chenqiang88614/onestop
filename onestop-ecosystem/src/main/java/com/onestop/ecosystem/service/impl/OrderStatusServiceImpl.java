package com.onestop.ecosystem.service.impl;

import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.service.IOrderStatusService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 订单状态实现类
 * @author: chenq
 * @date: 2019/9/6 8:49
 */
@Service("orderStatusService")
public class OrderStatusServiceImpl implements IOrderStatusService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Map list(String name) {
        String hkey = RedisKey.STATUS_PREX.getKey() + name;
        Map<String, Map> result = new HashMap<>(4);
        Map status = redisTemplate.opsForHash().entries(hkey);
        result.put("status", status);
        Map thematicType = redisTemplate.opsForHash().entries(RedisKey.THEMATIC_TYPE.getKey());
        result.put("thematicType", thematicType);
        if (StringUtils.equals(hkey, RedisKey.PROD_ORDER_STATUS.getKey())) {
            Map<String, String> urgencyLevel = new HashMap<>(2);
            urgencyLevel.put("0", "一般");
            urgencyLevel.put("1", "紧急");
            result.put("urgencyLevel", urgencyLevel);
        } else if (StringUtils.equals(hkey, RedisKey.PRE_PROCESS_ORDER_STATUS.getKey())) {

        }
        return result;
    }
}

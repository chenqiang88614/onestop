package com.onestop.ecosystem.init;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.onestop.ecosystem.constant.PreprocessStatus;
import com.onestop.ecosystem.constant.ProdOrderStatus;
import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.entity.PreprocessOrder;
import com.onestop.ecosystem.entity.ProdOrder;
import com.onestop.ecosystem.service.IPreprocessOrderService;
import com.onestop.ecosystem.service.IProdOrderService;
import com.onestop.ecosystem.util.RedisObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 初始化时对任务状态加载到redis中，保证一致性
 * @author: chenq
 * @date: 2019/9/9 14:02
 */
@Component
@Order(4)
@Slf4j
public class TaskStatusInit implements CommandLineRunner {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private IProdOrderService prodOrderService;

    @Resource
    private IPreprocessOrderService preprocessOrderService;

    @Override
    public void run(String... args) throws Exception {
        LambdaQueryWrapper<ProdOrder> prodOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
        prodOrderLambdaQueryWrapper.eq(ProdOrder::getStatus, ProdOrderStatus.PROCESSING.getKey());
        List<ProdOrder> list = prodOrderService.list(prodOrderLambdaQueryWrapper);
        if (list != null) {
            Map<String, String> preprocessOrderStatus =
                    (Map) redisTemplate.opsForHash().entries(RedisKey.PRE_PROCESS_ORDER_STATUS.getKey());
            list.forEach(prodOrder -> {
                // 查找该id下所有订单
                String orderId = prodOrder.getOrderId();
                LambdaQueryWrapper<PreprocessOrder> preprocessOrderLambdaQueryWrapper = new LambdaQueryWrapper<>();
                preprocessOrderLambdaQueryWrapper.eq(PreprocessOrder::getOrderId, orderId);
                List<PreprocessOrder> preprocessOrderList = preprocessOrderService.list(preprocessOrderLambdaQueryWrapper);
                AtomicInteger count = new AtomicInteger();
                preprocessOrderList.forEach(preprocessOrder -> {
                    Integer status = preprocessOrder.getStatus();
                    RedisObject redisObject = new RedisObject();
                    redisObject.setStatus(status);
                    redisObject.setProductId(preprocessOrder.getProductId());
                    if (status < 6) {
                        // 状态小于6表示正在执行
                    } else {
                        // 正常或异常执行结束
                        redisObject.setReason(preprocessOrderStatus.get(String.valueOf(status)));
                        count.getAndIncrement();
                    }
                    if (count.get() > 0) {
                        redisTemplate.opsForHash().put(RedisKey.SUB_ORDER_COUNT_PREX.getKey(),
                                preprocessOrder.getOrderId(), count.get());
                    }
                });
            });
        }
    }
}

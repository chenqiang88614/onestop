package com.onestop.ecosystem.scanner;

import com.onestop.common.util.SnowflakeIdWorker;
import com.onestop.common.util.SpringContextHolder;
import com.onestop.ecosystem.bean.ExtractReport;
import com.onestop.ecosystem.constant.PreprocessStatus;
import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.constant.SystemName;
import com.onestop.ecosystem.entity.CommonOrder;
import com.onestop.ecosystem.entity.PreprocessOrder;
import com.onestop.ecosystem.service.ICommonOrderService;
import com.onestop.ecosystem.service.IPreprocessOrderService;
import com.onestop.fsf.api.AbstractFileObserver;
import com.onestop.xml.util.PojoReflect;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.File;
import java.time.LocalDateTime;

/**
 * @description: 数据提取订单完成报告解析
 * @author: chenq
 * @date: 2019/9/6 16:24
 */
@Slf4j
public class ExtractReportFileObserver extends AbstractFileObserver {

    @Override
    public void doHandler(File srcFile) {
         ICommonOrderService commOrderService = SpringContextHolder
                .getBean("commonOrderService");
         RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");
         IPreprocessOrderService preprocessOrderService = SpringContextHolder.getBean("preprocessOrderService");
         SnowflakeIdWorker snowflakeIdWorker = SpringContextHolder.getBean("snowflakeIdWorker");

        log.info("接收到 {} 发送的数据提取订单完成报告，文件保存在：{}", SystemName.DMS.getName(), srcFile.getAbsolutePath());
        try {
            OrderFileResolve orderFileResolve = new OrderFileResolve(srcFile);
            CommonOrder commonOrder = orderFileResolve.getCommonOrder();
            PojoReflect pojoReflect = orderFileResolve.getPojoReflect(ExtractReport.class);

            ExtractReport extractReport = (ExtractReport) pojoReflect.getInstance();
            String orderId = extractReport.getOrderId();
            String productId = extractReport.getProductId();
            if (StringUtils.isNotEmpty(orderId) && StringUtils.isNotEmpty(productId)) {
                PreprocessOrder preprocessOrder = preprocessOrderService.getByOrderIdAndProductId(orderId, productId);
                if (preprocessOrder != null) {
                    // 进行拒绝处理
                    String reject = "1";
                    if (StringUtils.equals(extractReport.getStatus(), reject)) {
                        log.info("{} 拒绝订单号为 {}，数据ID为 {} 的数据提取，原因：{}", SystemName.DMS.getName(), extractReport.getOrderId(),
                                extractReport.getProductId(), extractReport.getReason());
                        preprocessOrderService.modifyTaskStatus(preprocessOrder.getOrderId(),
                                preprocessOrder.getTaskId(), 1,
                                PreprocessStatus.EXTRACT_DATA_FAIL.getName() + "：" + extractReport.getReason());

                        preprocessOrder.setStatus(PreprocessStatus.EXTRACT_DATA_FAIL.getKey());
                        preprocessOrder.setReason(extractReport.getReason());

                        long count = redisTemplate.opsForHash().increment(RedisKey.SUB_ORDER_COUNT_PREX.getKey(),
                                orderId, -1L);
                        if (count == 0) {
                            log.debug("订单 {} 所有子订单执行完毕。", extractReport.getOrderId());
                            preprocessOrderService.completeTask(preprocessOrder);
                        }
                    } else {
                        String dataPath = extractReport.getDataPath();
                        dataPath = StringUtils.replace(dataPath, "\\", "/");
                        String[] split = StringUtils.split(dataPath, "/");
                        String dataName = split[split.length - 1];
                        preprocessOrder.setDataPath(dataPath);
                        preprocessOrder.setDataName(dataName);
                        preprocessOrder.setStatus(PreprocessStatus.EXTRACT_DATA_FIN.getKey());
                    }
                    preprocessOrder.setUpdateTime(LocalDateTime.now());
                    preprocessOrderService.saveOrUpdate(preprocessOrder);
                    log.info("订单号为 {}，数据ID为 {} 的数据提取完成。", preprocessOrder.getOrderId(), preprocessOrder.getProductId());

                    commonOrder.setId(snowflakeIdWorker.nextId());
                    commonOrder.setOrderId(Long.parseLong(preprocessOrder.getOrderId()));
                    commonOrder.setSatelliteId(preprocessOrder.getSatelliteId());
                    commOrderService.save(commonOrder);
                } else {
                    log.info("根据数据提取订单完成报告中的orderId和productId查询数据库为空，文件：{}", srcFile.getName());
                }
            } else {
                log.info("数据提取订单完成报告中的orderId或者productId为空，文件：{}", srcFile.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("数据提取订单完成报告处理异常，文件 {}，原因：{}", srcFile.getName(), e.getLocalizedMessage());
        }
    }

    @Override
    public void setParmString(String s) {

    }
}

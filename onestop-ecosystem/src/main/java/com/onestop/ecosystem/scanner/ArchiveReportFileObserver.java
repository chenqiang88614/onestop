package com.onestop.ecosystem.scanner;

import com.onestop.common.util.SnowflakeIdWorker;
import com.onestop.common.util.SpringContextHolder;
import com.onestop.ecosystem.bean.ArchiveReport;
import com.onestop.ecosystem.constant.PreprocessStatus;
import com.onestop.ecosystem.constant.RedisKey;
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
 * @description: 专题产品定制完成报告文件解析
 * @author: chenq
 * @date: 2019/9/9 13:13
 */
@Slf4j
public class ArchiveReportFileObserver extends AbstractFileObserver {
    private RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");
    private SnowflakeIdWorker snowflakeIdWorker = SpringContextHolder.getBean("snowflakeIdWorker");
    @Override
    public void doHandler(File srcFile) {
        log.info("接收到DMS发送的专题产品归档完成报告，文件保存在：{}", srcFile.getAbsolutePath());
        try {
            ICommonOrderService commonOrderService = SpringContextHolder.getBean("commonOrderService");
            IPreprocessOrderService preprocessOrderService = SpringContextHolder.getBean("preprocessOrderService");

            OrderFileResolve orderFileResolve = new OrderFileResolve(srcFile);
            CommonOrder commonOrder = orderFileResolve.getCommonOrder();
            PojoReflect pojoReflect = orderFileResolve.getPojoReflect(ArchiveReport.class);
            ArchiveReport archiveReport = (ArchiveReport) pojoReflect.getInstance();
            String taskId = archiveReport.getTaskId();
            if (StringUtils.isNotEmpty(taskId)) {
                PreprocessOrder preprocessOrder = preprocessOrderService.getByTaskId(taskId);
                if (preprocessOrder != null) {
                    commonOrder.setOrderId(preprocessOrder.getId());
                    String status = archiveReport.getStatus();
                    if (StringUtils.equals(status, "0")) {
                        log.info("专题产品归档完成报告成功，文件为：{}", srcFile.getAbsolutePath());
                        preprocessOrder.setStatus(PreprocessStatus.ARCHIVE_SUCCESS.getKey());
                    } else {
                        log.info("专题产品归档完成报告失败，TaskID = {}, 原因：{}", archiveReport.getTaskId(), archiveReport.getReason());
                        preprocessOrder.setStatus(PreprocessStatus.ARCHIVE_FAIL.getKey());
                        preprocessOrder.setReason(archiveReport.getReason());
                        preprocessOrderService.modifyTaskStatus(archiveReport.getOrderId(), archiveReport.getTaskId(),
                                1, PreprocessStatus.ARCHIVE_FAIL.getName() + "：" + archiveReport.getReason());
                        redisTemplate.opsForHash().increment(RedisKey.SUB_ORDER_COUNT_PREX.getKey(), archiveReport.getOrderId(),
                                -1L);
                    }
                    preprocessOrder.setUpdateTime(LocalDateTime.now());
                } else {
                    log.error("专题产品归档完成报告 {} 中的TaskID = {} 在数据库中无记录。", srcFile.getName(), taskId);
                }
                commonOrder.setId(snowflakeIdWorker.nextId());
                commonOrderService.save(commonOrder);
                preprocessOrderService.saveOrUpdate(preprocessOrder);
                // 最后将redis中的计数器减一
                Long count = redisTemplate.opsForHash().increment(RedisKey.SUB_ORDER_COUNT_PREX.getKey(),
                        preprocessOrder.getOrderId(), -1L);
                if (count == 0L) {
                    // 表示该OrderID的定制单所有的生产均已完成并归档，可以向PSS发送专题产品定制完成报告
                    preprocessOrderService.completeTask(preprocessOrder);
                }
            } else {
                log.error("专题产品归档完成报告 {} 中的TaskID字段为空。", srcFile.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setParmString(String s) {

    }

}

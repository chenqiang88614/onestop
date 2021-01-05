package com.onestop.ecosystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyLambdaQueryWrapper;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.common.service.impl.ServiceWithRedisImpl;
import com.onestop.common.util.BeanMapper;
import com.onestop.common.util.SnowflakeIdWorker;
import com.onestop.common.util.SpringContextHolder;
import com.onestop.ecosystem.bean.ArchiveOrder;
import com.onestop.ecosystem.bean.ProdOrderReport;
import com.onestop.ecosystem.constant.PreprocessStatus;
import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.entity.ProdOrder;
import com.onestop.ecosystem.service.IFileSendService;
import com.onestop.ecosystem.util.RedisObject;
import com.onestop.ecosystem.vo.ConfirmVo;
import com.onestop.ecosystem.vo.PreprocessOrderVo;
import com.onestop.ecosystem.vo.ProdOrderVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onestop.ecosystem.entity.PreprocessOrder;
import com.onestop.ecosystem.mapper.PreprocessOrderMapper;
import com.onestop.ecosystem.service.IPreprocessOrderService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Service("preprocessOrderService")
@Slf4j
public class PreprocessOrderServiceImpl extends ServiceWithRedisImpl<PreprocessOrderMapper, PreprocessOrder> implements IPreprocessOrderService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private IFileSendService fileSendService;

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public MyPage<PreprocessOrderVo> list(Page page, HttpServletRequest request) {
        if (page.ascs() == null && page.descs() == null) {
            page.setDesc("task_id");
        }
        MyLambdaQueryWrapper<PreprocessOrder> queryWrapper = this.createQueryWrapper(request);
        MyPage<PreprocessOrder> preprocessOrderMyPage = this.page(queryWrapper, page, true);
        MyPage<PreprocessOrderVo> preprocessOrderVoMyPage = new MyPage<>();
        preprocessOrderVoMyPage.setTotal(preprocessOrderMyPage.getTotal());
        List<PreprocessOrder> preprocessOrderList = preprocessOrderMyPage.getRecords();
        List<PreprocessOrderVo> preprocessOrderVoList = BeanMapper.mapList(preprocessOrderList,
                PreprocessOrderVo.class);
        Map<String, String> statusMap = (Map) redisTemplate.opsForHash().entries(RedisKey.PRE_PROCESS_ORDER_STATUS.getKey());
        preprocessOrderVoList.forEach(preprocessOrderVo -> {
            String thematicName = (String) redisTemplate.opsForHash().get(RedisKey.THEMATIC_TYPE.getKey(),
                    preprocessOrderVo.getThematicType());
            preprocessOrderVo.setThematicName(thematicName);
            preprocessOrderVo.setStatus(statusMap.get(preprocessOrderVo.getStatus()));
        });
        preprocessOrderVoMyPage.setRecords(preprocessOrderVoList);
        return preprocessOrderVoMyPage;
    }

    @Override
    public PreprocessOrder getByOrderIdAndProductId(String orderId, String productId) {
        log.info("orderId = {}, productId = {}", orderId, productId);
        LambdaQueryWrapper<PreprocessOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PreprocessOrder::getOrderId, orderId).eq(PreprocessOrder::getProductId, productId);
        return this.getOne(queryWrapper);
    }

    @Override
    public PreprocessOrder getByTaskId(String taskId) {
        LambdaQueryWrapper<PreprocessOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PreprocessOrder::getTaskId, taskId);
        return this.getOne(queryWrapper);
    }

    @Override
    public void modifyTaskStatus(String orderId, String taskId, Integer status, String reason) {
        String resultKey = RedisKey.SUB_ORDER_RESULT_PREX.getKey() + orderId;
        RedisObject redisObject = (RedisObject) redisTemplate.opsForHash().get(resultKey, taskId);
        redisObject.setStatus(status);
        redisObject.setReason(reason);
        redisTemplate.opsForHash().put(resultKey, taskId, redisObject);
    }

    @Override
    public void completeTask(PreprocessOrder preprocessOrder) {
        log.info("OrderId 为 {} 的所有拆分子订单均已执行完毕，开始发送专题产品定制完成报告。", preprocessOrder.getOrderId());
        ProdOrderReport prodOrderReport = new ProdOrderReport();
        prodOrderReport.setOrderId(preprocessOrder.getOrderId());
        prodOrderReport.setSatelliteId(preprocessOrder.getSatelliteId());
        prodOrderReport.setOperatorName("AUTOMATION");

        String hkey = RedisKey.SUB_ORDER_RESULT_PREX.getKey() + preprocessOrder.getOrderId();
        Map<String, RedisObject> resultMap = (Map) redisTemplate.opsForHash().entries(hkey);
        StringBuilder sb = new StringBuilder();
        AtomicReference<Boolean> fail = new AtomicReference<>(false);
        resultMap.forEach((k, v) -> {
            if (v.getStatus() == 1) {
                sb.append("源数据ID = ").append(v.getProductId()).append(" 未成功生产专题产品，原因：").append(v.getReason()).append(
                        "\\n");
            } else {
                fail.updateAndGet(v1 -> true);
            }
        });
        if (sb.length() == 0) {
            preprocessOrder.setStatus(0);
        } else if (fail.get()) {
            // 部分成功
            preprocessOrder.setStatus(2);
            preprocessOrder.setReason(sb.toString());
        } else {
            // 全部失败
            preprocessOrder.setStatus(1);
            preprocessOrder.setReason(sb.toString());
        }
        fileSendService.sendProdOrderReport(prodOrderReport, preprocessOrder.getId());
    }

    @Override
    public void consumMessage(Message message, byte[] pattern) {
        Object value = redisTemplate.getValueSerializer().deserialize(message.getBody());
        try {
            Map<String, String> map = (Map) value;
            Objects.requireNonNull(map).forEach((k, v) -> System.out.println(k + " = " + v));
            String id = map.get("id");
            String result = map.get("result");
            String reason = map.get("reason");
            String dataPath = map.get("dataPath");
            String success = "0";
            if (StringUtils.isNotEmpty(id)) {
                PreprocessOrder preprocessOrder = this.getById(id);
                if (preprocessOrder != null) {
                    if (StringUtils.equals(result, success)) {
                        preprocessOrder.setStatus(PreprocessStatus.PROCESS_FIN.getKey());

                        ArchiveOrder archiveOrder = new ArchiveOrder();
                        archiveOrder.setOperatorName("AUTOMATION");
                        archiveOrder.setTaskId(preprocessOrder.getTaskId());
                        archiveOrder.setArchiveType("0");
                        archiveOrder.setSatelliteId(preprocessOrder.getSatelliteId());
                        archiveOrder.setOrderId(preprocessOrder.getOrderId());
                        archiveOrder.setFileName(StringUtils.replace(dataPath, "\\", "/"));
                        preprocessOrder.setResultPath(dataPath);
                        fileSendService.sendArchiveOrder(archiveOrder, preprocessOrder.getId());
                    } else {
                        preprocessOrder.setStatus(PreprocessStatus.PROCESS_FAIL.getKey());
                        preprocessOrder.setReason(reason);
                        this.modifyTaskStatus(preprocessOrder.getOrderId(), preprocessOrder.getTaskId(), 1, reason);

                        Long count = redisTemplate.opsForHash().increment(RedisKey.SUB_ORDER_COUNT_PREX.getKey(),
                                preprocessOrder.getOrderId(), -1L);
                        if (count == 0L) {
                            // 表示该OrderID的定制单所有的生产均已完成并归档，可以向PSS发送专题产品定制完成报告
                            this.completeTask(preprocessOrder);
                        }
                    }
                    preprocessOrder.setUpdateTime(LocalDateTime.now());
                    this.saveOrUpdate(preprocessOrder);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("主题 {} 接收到错误信息，该信息为 {}", new String(pattern), value);
        }
    }

    @Override
    public String doProcess(String id) {
        if (StringUtils.isNotEmpty(id)) {
            PreprocessOrder preprocessOrder = this.getById(id);
            if (preprocessOrder != null) {
                preprocessOrder.setUpdateTime(LocalDateTime.now());
                preprocessOrder.setStatus(PreprocessStatus.PROCESS.getKey());
                this.saveOrUpdate(preprocessOrder);
                log.info("预处理订单 {} 开始处理。", id);
                return null;
            } else {
                log.error("所提供的id = {} 的预处理订单不存在。", id);
                return "所提供的id = " + id + " 的预处理订单不存在。";
            }
        } else {
            log.error("提交的id为空");
            return "id为空。";
        }
    }

    private MyLambdaQueryWrapper<PreprocessOrder> createQueryWrapper(HttpServletRequest request) {
        MyLambdaQueryWrapper<PreprocessOrder> queryWrapper = new MyLambdaQueryWrapper<>();
        String id = request.getParameter("id");
        if (StringUtils.isNotEmpty(id)) {
            queryWrapper.eq(PreprocessOrder::getProdorderId, Long.parseLong(id));
        }

        String status = request.getParameter("status");
        if (StringUtils.isNotEmpty(status)) {
            queryWrapper.eq(PreprocessOrder::getStatus, Integer.parseInt(status));
        }

        String orderId = request.getParameter("orderId");
        if (StringUtils.isNotEmpty(orderId)) {
            queryWrapper.eq(PreprocessOrder::getOrderId, orderId);
        }
        return queryWrapper;
    }
}

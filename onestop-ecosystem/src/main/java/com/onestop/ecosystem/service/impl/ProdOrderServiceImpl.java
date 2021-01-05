package com.onestop.ecosystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyLambdaQueryWrapper;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.common.service.impl.ServiceWithRedisImpl;
import com.onestop.common.util.BeanMapper;
import com.onestop.common.util.SearchUtil;
import com.onestop.common.util.SnowflakeIdWorker;
import com.onestop.ecosystem.bean.ProdOrderAck;
import com.onestop.ecosystem.constant.PreprocessStatus;
import com.onestop.ecosystem.constant.ProdOrderStatus;
import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.constant.SystemName;
import com.onestop.ecosystem.entity.CommonOrder;
import com.onestop.ecosystem.entity.PreprocessOrder;
import com.onestop.ecosystem.mapper.ProdOrderMapper;
import com.onestop.ecosystem.entity.ProdOrder;
import com.onestop.ecosystem.service.ICommonOrderService;
import com.onestop.ecosystem.service.IFileSendService;
import com.onestop.ecosystem.service.IPreprocessOrderService;
import com.onestop.ecosystem.service.IProdOrderService;
import com.onestop.ecosystem.util.RedisObject;
import com.onestop.ecosystem.vo.ConfirmVo;
import com.onestop.ecosystem.vo.ProdOrderVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("prodOrderService")
@Slf4j
public class ProdOrderServiceImpl extends ServiceWithRedisImpl<ProdOrderMapper, ProdOrder> implements IProdOrderService {
    @Resource
    private IFileSendService fileSendService;

    @Resource
    private ICommonOrderService commonOrderService;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private IPreprocessOrderService preprocessOrderService;

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;
    @Override
    public MyPage<ProdOrder> getPage(Page page, HttpServletRequest request) {
        if (page.ascs() == null && page.descs() == null) {
            page.setDesc("order_id");
        }
        MyLambdaQueryWrapper<ProdOrder> queryWrapper = this.createQueryWrapper(request);
        MyPage<ProdOrder> prodOrderMyPage = this.page(queryWrapper, page, true);
        return prodOrderMyPage;
    }

    @Override
    public MyPage<ProdOrderVo> getPageWithVo(Page page, HttpServletRequest request) {
        MyPage<ProdOrder> prodOrderMyPage = this.getPage(page, request);
        MyPage<ProdOrderVo> result = new MyPage<>();
        result.setTotal(prodOrderMyPage.getTotal());
        List<ProdOrderVo> prodOrderVoList = BeanMapper.mapList(prodOrderMyPage.getRecords(), ProdOrderVo.class);
        result.setRecords(prodOrderVoList);
        Map<String, String> statusMap = (Map) redisTemplate.opsForHash().entries(RedisKey.PROD_ORDER_STATUS.getKey());
        prodOrderVoList.forEach(prodOrderVo -> {
            String urgencyLevel = prodOrderVo.getUrgencyLevel();
            if (StringUtils.isNotEmpty(urgencyLevel)) {
                if (Integer.parseInt(urgencyLevel) == 1) {
                    prodOrderVo.setUrgencyLevel("紧急");
                } else {
                    prodOrderVo.setUrgencyLevel("一般");
                }
            }
            try {
                String thematicType = (String) redisTemplate.opsForHash().get(RedisKey.THEMATIC_TYPE.getKey(),
                        prodOrderVo.getThematicType());
                prodOrderVo.setThematicType(thematicType);
            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getLocalizedMessage());
            }
            prodOrderVo.setStatus(statusMap.get(prodOrderVo.getStatus()));
        });
        return result;
    }

    @Override
    public String confirm(ConfirmVo confirm) {
        ProdOrder prodOrder = this.getById(confirm.getId());
        if (prodOrder == null) {
            log.info("专题产品定制订单id {} 不存在。", confirm.getId());
            return "专题产品定制订单id = " + confirm.getId() +" 的数据不存在！";
        }
        // 向PSS发送订单确认
        log.debug("向PSS发送产品订单确认，订单orderID = {}", prodOrder.getOrderId());
        ProdOrderAck prodOrderAck = new ProdOrderAck();
        prodOrderAck.setOrginalId(String.valueOf(prodOrder.getId()));
        prodOrderAck.setOperatorName(confirm.getCurrentUser());
        prodOrderAck.setOrderId(prodOrder.getOrderId());
        prodOrderAck.setSatelliteId(prodOrder.getSatelliteId());
        if (confirm.isConfirm()) {
            prodOrderAck.setStatus("0");
            prodOrder.setStatus(ProdOrderStatus.PROCESSING.getKey());
        } else {
            prodOrderAck.setStatus("1");
            prodOrderAck.setReason(confirm.getReason());
            prodOrder.setStatus(ProdOrderStatus.REJECT.getKey());
        }
        prodOrder.setUpdateTime(LocalDateTime.now());
        this.saveOrUpdate(prodOrder);
        String result = fileSendService.sendProdOrderAck(prodOrderAck, confirm.isConfirm(), SystemName.TAS.getAlias(),
                SystemName.PSS.getAlias());

        // 拆分订单，并下发数据提取单
        threadPoolTaskExecutor.execute(() -> orderSplit(prodOrder, confirm.getCurrentUser()));
        return result;
    }

    @Override
    public Boolean checkExistByOrderId(String orderId) {
        LambdaQueryWrapper<ProdOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProdOrder::getOrderId, orderId);
        int count = this.count(queryWrapper);
        if (count == 0) {
            return true;
        }
        return false;
    }

    private MyLambdaQueryWrapper<ProdOrder> createQueryWrapper(HttpServletRequest request) {
        MyLambdaQueryWrapper<ProdOrder> queryWrapper = new MyLambdaQueryWrapper<>();
        String satelliteId = request.getParameter("satelliteId");
        if (StringUtils.isNotEmpty(satelliteId)) {
            queryWrapper.eq(ProdOrder::getSatelliteId, satelliteId);
        }

        String orderId = request.getParameter("orderId");
        if (StringUtils.isNotEmpty(orderId)) {
            queryWrapper.eq(ProdOrder::getOrderId, orderId);
        }

        String urgencyLevel = request.getParameter("urgencyLevel");
        if (StringUtils.isNotEmpty(urgencyLevel)) {
            if (StringUtils.equals(urgencyLevel, "一般")) {
                queryWrapper.eq(ProdOrder::getUrgencyLevel, 0);
            } else {
                queryWrapper.eq(ProdOrder::getUrgencyLevel, 1);
            }
        }

        String thematicType = request.getParameter("thematicType");
        if (StringUtils.isNotEmpty(thematicType)) {
            queryWrapper.eq(ProdOrder::getThematicType, thematicType);
        }

        String status = request.getParameter("status");
        if (StringUtils.isNotEmpty(status)) {
            queryWrapper.eq(ProdOrder::getStatus, Integer.parseInt(status));
        }

        List<LocalDateTime> localDateTimes = SearchUtil.findByTime(request);
        if (null != localDateTimes) {
            queryWrapper.between(ProdOrder::getUpdateTime, localDateTimes.get(0), localDateTimes.get(1));
        }
        return queryWrapper;
    }

    /**
     * @description: 根据productId或者ScenceId拆分订单
     * @author: chenq
     * @date: 2019年9月5日11:16:10
     */
    private void orderSplit(ProdOrder prodOrder, String currentUser) {
        log.info("开始对专题产品定制单 {} 进行拆分处理。", prodOrder.getOrderId());
        if (StringUtils.isNotEmpty(prodOrder.getProductId())) {
            String[] productIdList = StringUtils.split(prodOrder.getProductId(), ",");
            List<PreprocessOrder> preprocessOrderList = new ArrayList<>(8);
            for (int i = 0; i < productIdList.length; i++) {
                String productId = productIdList[i];
                PreprocessOrder preprocessOrder = new PreprocessOrder();
                preprocessOrder.setId(snowflakeIdWorker.nextId());
                preprocessOrder.setOrderId(prodOrder.getOrderId());
                preprocessOrder.setProdorderId(prodOrder.getId());
                preprocessOrder.setProductId(productId);
                preprocessOrder.setThematicType(prodOrder.getThematicType());
                preprocessOrder.setTaskId(prodOrder.getOrderId() + "#" + (i + 1));
                preprocessOrder.setCreationTime(LocalDateTime.now());
                preprocessOrder.setUpdateTime(LocalDateTime.now());
                preprocessOrder.setSatelliteId(prodOrder.getSatelliteId());
                preprocessOrder.setOperatorName(currentUser);
                preprocessOrder.setStatus(PreprocessStatus.EXTRACT_DATA.getKey());
                preprocessOrderList.add(preprocessOrder);
                //FIXME 下发数据提取订单,多线程是ftp报错remote path not found!，原因暂时没有找到
//                threadPoolTaskExecutor.execute(() -> fileSendService.sendExtractOrder(preprocessOrder));
                fileSendService.sendExtractOrder(preprocessOrder);
                RedisObject redisObject = new RedisObject();
                redisObject.setStatus(0);
                redisObject.setProductId(preprocessOrder.getProductId());
                String resultKey = RedisKey.SUB_ORDER_RESULT_PREX.getKey() + preprocessOrder.getOrderId();
                redisTemplate.opsForHash().put(resultKey, preprocessOrder.getTaskId(), redisObject);
            }
            boolean saveBatch = preprocessOrderService.saveBatch(preprocessOrderList);
            if (saveBatch) {
                redisTemplate.opsForHash().put(RedisKey.SUB_ORDER_COUNT_PREX.getKey(), prodOrder.getOrderId(),
                        productIdList.length);

            }

        }
    }
}

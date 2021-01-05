package com.onestop.ecosystem.scanner;

import com.onestop.common.util.SnowflakeIdWorker;
import com.onestop.common.util.SpringContextHolder;
import com.onestop.ecosystem.bean.ProdOrderAck;
import com.onestop.ecosystem.constant.ProdOrderStatus;
import com.onestop.ecosystem.entity.CommonOrder;
import com.onestop.ecosystem.entity.ProdOrder;
import com.onestop.ecosystem.service.ICommonOrderService;
import com.onestop.ecosystem.service.IFileSendService;
import com.onestop.ecosystem.service.IProdOrderService;
import com.onestop.fsf.api.AbstractFileObserver;
import com.onestop.xml.util.PojoReflect;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.File;
import java.time.LocalDateTime;

/**
 * @description 专题产品定制单ProdOrder xml文件解析
 * @author Administrator
 * @date 2019年9月3日08:56:29
 */
@Slf4j
public class ProdOrderFileObserver extends AbstractFileObserver {

    private IProdOrderService prodOrderService =  SpringContextHolder
            .getBean("prodOrderService");
    private ICommonOrderService commOrderService = SpringContextHolder
            .getBean("commonOrderService");
    private RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");
    private IFileSendService fileSendService = SpringContextHolder.getBean("fileSendService");
    private SnowflakeIdWorker snowflakeIdWorker = SpringContextHolder.getBean("snowflakeIdWorker");

    @Override
    public void setParmString(String s) {

    }

    @Override
    public void doHandler(File srcFile) {
        log.info("接收到PSS发送的专题产品定制单，文件保存在：{}", srcFile.getAbsolutePath());
        try {
            OrderFileResolve orderFileResolve = new OrderFileResolve(srcFile);
            CommonOrder commonOrder = orderFileResolve.getCommonOrder();
            PojoReflect pojoReflect = orderFileResolve.getPojoReflect(ProdOrder.class);
            ProdOrder prodOrder = (ProdOrder) pojoReflect.getInstance();

            log.debug("检查专题定制单 {}", srcFile.getName());
            boolean checkOrder = checkOrder(prodOrder, srcFile.getName(), commonOrder.getRecipient(), commonOrder.getOriginator());
            if (!checkOrder) {
                return;
            }

            prodOrder.setId(snowflakeIdWorker.nextId());
            prodOrder.setUpdateTime(LocalDateTime.now());
            prodOrder.setStatus(ProdOrderStatus.NO_PROCESS.getKey());
            prodOrder.setEnable(true);
            // FIXME 缺少aera字段的存储

            commonOrder.setId(snowflakeIdWorker.nextId());
            commonOrder.setOrderId(prodOrder.getId());
            prodOrder.setCommonId(commonOrder.getId());

            commOrderService.saveOrUpdate(commonOrder);
            prodOrderService.save(prodOrder);
            log.info("专题产品定制单: {} 入库保存成功。", srcFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("专题产品定制单处理出错，文件：{}", srcFile.getAbsolutePath());
            log.error("原因：{}", e.getLocalizedMessage());
        }
    }

    private boolean checkOrder(ProdOrder prodOrder, String fileName, String originator, String recipient) {
        // 检查是否orderId重复
        boolean checkOrderId = prodOrderService.checkExistByOrderId(prodOrder.getOrderId());
        boolean passed = true;
        String reason = "";
        if (!checkOrderId) {
            log.info("接收到PSS发送的专题产品订单中的OrderID重复，文件 {}", fileName);
            reason = "OrderID重复";
            passed = false;
        }

        // 检查订单中SceneID或者ProductID是否都不存在
        if (StringUtils.isEmpty(prodOrder.getSceneId()) && StringUtils.isEmpty(prodOrder.getProductId())) {
            log.info("接收到PSS发送的专题产品订单中SceneID和ProductID均不存在，文件 {}", fileName);
            reason = "SceneID和ProductID均不存在";
            passed = false;
        }

        if (!passed) {
            ProdOrderAck prodOrderAck = new ProdOrderAck();
            prodOrderAck.setOrderId(prodOrder.getOrderId());
            prodOrderAck.setReason(reason);
            prodOrderAck.setStatus("1");
            prodOrderAck.setSatelliteId(prodOrder.getSatelliteId());
            prodOrderAck.setOperatorName("AUTOMATION");
            prodOrderAck.setOrginalId(null);
            fileSendService.sendProdOrderAck(prodOrderAck, false, originator, recipient);
        }
        return passed;
    }
}

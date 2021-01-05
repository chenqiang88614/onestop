package com.onestop.ecosystem.service.impl;

import com.onestop.common.util.SnowflakeIdWorker;
import com.onestop.ecosystem.bean.ArchiveOrder;
import com.onestop.ecosystem.bean.ProdOrderAck;
import com.onestop.ecosystem.bean.ProdOrderReport;
import com.onestop.ecosystem.constant.FileMessageType;
import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.constant.SystemName;
import com.onestop.ecosystem.entity.CommonOrder;
import com.onestop.ecosystem.entity.PreprocessOrder;
import com.onestop.ecosystem.entity.ProdOrder;
import com.onestop.ecosystem.service.ICommonOrderService;
import com.onestop.ecosystem.service.IFileSendService;
import com.onestop.ecosystem.service.ISequenceFactory;
import com.onestop.ftpclient.service.FtpParamService;
import com.onestop.ftpclient.spi.FtpOperationFactory;
import com.onestop.ftpclient.util.FtpParam;
import com.onestop.xml.api.IXmlFileAdapter;
import com.onestop.xml.impl.BeanComponentInterfaceFactory;
import com.onestop.xml.model.Node;
import com.onestop.xml.template.BeanTemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @description: 文件发送实现类
 * @author: chenq
 * @date: 2019/9/4 15:15
 */
@Slf4j
@Service("fileSendService")
public class FileSendServiceImpl implements IFileSendService {
    @Value("${Configuration.customer.fsf.ftpfile-backup}")
    private String ftpBackupPath;
    @Resource
    private ISequenceFactory sequenceFactory;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private ICommonOrderService commonOrderService;

    @Resource
    private FtpParamService ftpParamService;

    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    private String messageType;

    private String messageId;

    private String originator;

    private String recipient;

    private String filePath;

    private String fileName;

    private CommonOrder commonOrder = new CommonOrder();

    @Override
    public String sendProdOrderAck(ProdOrderAck prodOrderAck, boolean confirm, String originator,
                                   String recipient) {
        messageType = FileMessageType.PROD_ORDER_ACK.getType();
        getMessageId();
        this.originator = originator;
        this.recipient = recipient;
        fileName = originator + "_" + recipient + "_" + prodOrderAck.getOrderId() + "_" + messageId + "." + messageType;
        filePath = generatPath(prodOrderAck.getOrderId(), messageType) + fileName;
        String result = doHandler(prodOrderAck);
        if (result == null) {
            commonOrder.setOrderId(Long.parseLong(prodOrderAck.getOrginalId()));
            commonOrder.setXmlPath(filePath);
            commonOrder.setSatelliteId(prodOrderAck.getSatelliteId());
            commonOrderService.save(commonOrder);
        }
        return result;
    }

    @Override
    public void sendExtractOrder(PreprocessOrder preprocessOrder) {
        messageType = FileMessageType.EXTRACT_ORDER.getType();
        getMessageId();
        fileName =
                SystemName.TAS.getAlias() + "_" + SystemName.DMS.getAlias() + "_" + preprocessOrder.getOrderId() + "_"
                        + messageId + "." + messageType;
        filePath = generatPath(preprocessOrder.getOrderId(), messageType) + fileName;
        originator = SystemName.TAS.getAlias();
        recipient = SystemName.DMS.getAlias();
        String result = doHandler(preprocessOrder);
        if (result == null) {
            commonOrder.setXmlPath(filePath);
            commonOrder.setSatelliteId(preprocessOrder.getSatelliteId());
            commonOrder.setOrderId(preprocessOrder.getId());
            commonOrderService.save(commonOrder);
        }
    }

    @Override
    public void sendProdOrderReport(ProdOrderReport prodOrderReport, long preprocessId) {
        this.messageType = FileMessageType.PROD_ORDER_REPORT.getType();
        getMessageId();
        this.originator = SystemName.TAS.getAlias();
        this.recipient = SystemName.PSS.getAlias();
        this.fileName = this.originator + "_" + this.recipient + "_" + prodOrderReport.getOrderId() + "_"
                        + this.messageId + "." + this.messageType;
        filePath = generatPath(prodOrderReport.getOrderId(), messageType) + fileName;
        String result = doHandler(prodOrderReport);
        if (result == null) {
            commonOrder.setXmlPath(filePath);
            commonOrder.setSatelliteId(prodOrderReport.getSatelliteId());
            commonOrder.setOrderId(preprocessId);
            commonOrderService.save(commonOrder);
        }

    }

    @Override
    public void sendArchiveOrder(ArchiveOrder archiveOrder, long preprocessId) {
        this.messageType = FileMessageType.ARCHIVE_ORDER.getType();
        getMessageId();
        this.originator = SystemName.TAS.getAlias();
        this.recipient = SystemName.DMS.getAlias();
        this.fileName =
                this.originator + "_" + this.recipient + "_" + archiveOrder.getOrderId() + "_" + this.messageId + "." + this.messageType;
        filePath = generatPath(archiveOrder.getOrderId(), messageType) + fileName;
        String result = doHandler(archiveOrder);
        if (result == null) {
            commonOrder.setXmlPath(filePath);
            commonOrder.setSatelliteId(archiveOrder.getSatelliteId());
            commonOrder.setOrderId(preprocessId);
            commonOrderService.save(commonOrder);
        }
    }

    private void getMessageId() {
        long redisMessageId = sequenceFactory.generate(RedisKey.MESSAGE_ID.getKey());
        messageId = String.format("%012d", redisMessageId);
    }

    private String doHandler(Object object) {
        IXmlFileAdapter xmlFile = BeanComponentInterfaceFactory.getXmlFileAdapter();
        Map<String, Node> map = BeanTemplateFactory.createIBean(messageType);
        Node bean = map.get(messageType);
        generaHeader(bean, messageType, originator, recipient, messageId);

        Node body = bean.getNodeValue("FileBody");
        generaBody(body, messageType, object);
        return sendFile(xmlFile, bean, filePath, fileName, recipient);
    }

    private void generaHeader(Node bean, String messageType, String originator, String recipient, String messageId) {
        Node header = bean.getNodeValue("FileHeader");
        header.setPropertyValue("MessageID", messageId);
        header.setPropertyValue("MessageType", messageType);
        header.setPropertyValue("Originator", originator);
        header.setPropertyValue("Recipient", recipient);
        LocalDateTime createTime = LocalDateTime.now();
        header.setPropertyValue("MessageCreateTime",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(createTime).replace(" ", "T"));
        commonOrder.setId(snowflakeIdWorker.nextId());
        commonOrder.setMessageId(messageId);
        commonOrder.setMessageType(messageType);
        commonOrder.setOriginator(originator);
        commonOrder.setRecipient(recipient);
        commonOrder.setMessageCreationTime(createTime);
    }

    private void generaBody(Node body, String messageType, Object object) {
        Map<String, String> mapping = (Map) redisTemplate.opsForHash().entries(RedisKey.XML_TO_DB_PREX.getKey() + messageType);
        mapping.forEach((k, v) -> {
            try {
                Field field = object.getClass().getDeclaredField(k);
                field.setAccessible(true);
                body.setPropertyValue(v, field.get(object));
            } catch (Exception e) {
                log.warn("数据提取单中字段 {} 存在问题。", k);
            }
        });
    }

    private String generatPath(String orderId, String type) {
        LocalDateTime localDateTime = LocalDateTime.now();
        String filePath =
                ftpBackupPath + "/" + localDateTime.getYear() + "/" + DateTimeFormatter.ofPattern("MM-dd").format(localDateTime) + "/" + orderId + "/" + type + "/";
        File folder = new File(filePath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return filePath;
    }

    private String sendFile(IXmlFileAdapter xmlFile, Node bean, String filePath, String filename, String recipient) {
        if (xmlFile.beanToFile(bean, filePath)) {
            FtpParam ftpParam = ftpParamService.getByServerName(recipient);
            if (ftpParam != null) {
                String result = FtpOperationFactory.getFtpOperation(ftpParam).uploadFile("/", filePath);
                if (result != null) {
                    log.error(result);
                    return result;
                }
                log.info("成功向 {} 发送文件 {}", recipient, filename);
            } else {
                String result = recipient + " 的FTP配置获取失败！";
                log.error(result);
                return result;
            }
        }
        return null;
    }

}

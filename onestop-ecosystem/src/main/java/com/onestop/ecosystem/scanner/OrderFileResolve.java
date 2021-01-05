package com.onestop.ecosystem.scanner;

import com.onestop.common.util.SpringContextHolder;
import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.entity.CommonOrder;
import com.onestop.xml.util.PojoReflect;
import com.onestop.xml.util.PojoReflectFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

/**
 * @author Administrator
 */
@Slf4j
public class OrderFileResolve {

    private final static DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private Element header, body;
    /**
     * CommOrder Pojo赋值类
     */
//    private PojoReflect comm = PojoReflectFactory.getInstance().getPojoReflect(CommonOrder.class);
    private String xmlPath;

    /**
     * OrderFileResolve
     * 构造函数，需要获取到该文件内容
     *
     * @param file
     */
    public OrderFileResolve(File file) {
        if (!file.exists()) {
            return;
        }
        SAXBuilder sb = new SAXBuilder();
        Document doc = null;

        xmlPath = file.getAbsolutePath();
        log.info("orderFileResolve init xml = {}", xmlPath );
        try {
            doc = sb.build(file);
            Element root = doc.getRootElement();
            header = root.getChild("FileHeader");
            body = root.getChild("FileBody");
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * getCommonOrder
     *
     * @描述: 从文件读取commonOrder部分的字段
     * @作者: liangjw
     * @创建时间: 2016-4-22上午10:16:42
     */
    public CommonOrder getCommonOrder() {
        PojoReflect comm = new PojoReflect(CommonOrder.class);
        for (String key : new String[]{"Recipient", "Originator", "MessageID", "MessageType"}) {
            comm.setValue(key, header.getChildText(key));
        }
        try {
            comm.setValue("satelliteId", body.getChildText("SatelliteID"));
        } catch (Exception e) {
            log.debug("文件 {} body中没有SatelliteID。", xmlPath);
        }
        comm.setValue("messageCreationTime", string2LocalDateTime(header.getChildText("MessageCreateTime")));
        if (System.getProperty("os.name").startsWith("Windows")) {
            xmlPath = StringUtils.replace(xmlPath, "\\", "/");
        }

        comm.setValue("xmlPath", xmlPath);
        return (CommonOrder) comm.getInstance();
    }

    /**
     * getPojoReflect
     *
     * @return 定制单对象
     * @description 从文件中读取PojoReflect，传入实体类
     * @author chenq
     * @date 2019年9月3日08:42:41
     */

    public PojoReflect getPojoReflect(Class<?> clazz) {
        if (header == null || body == null) {
            log.error("文件 {} 的body或者header为空！", xmlPath);
            return null;
        }
        String type = clazz.getSimpleName();
//        PojoReflect prod = PojoReflectFactory.getInstance().getPojoReflect(clazz);
        PojoReflect prod = new PojoReflect(clazz);
        String hkey = RedisKey.MODEL_TO_DB.getKey() + ":" + type.toUpperCase();
        RedisTemplate<String, Map> redisTemplate = SpringContextHolder.getBean("redisTemplate");
        Map<Object, Object> map = redisTemplate.opsForHash().entries(hkey);
        createPojoReflect(map, prod);
        log.info("从xml中获取PojoReflect成功，类型 {}, 原始文件: {}", type, xmlPath);
        return prod;
    }

    private void createPojoReflect(Map map, PojoReflect prod) {
        map.forEach((k, v) -> {
            try {
                String key = (String) k;
                String value = (String) v;
                switch (value) {
                    case "String":
                        prod.setValue(key, body.getChildText(key));
                        break;
                    case "Double":
                    case "double":
                        prod.setValue(key, Double.parseDouble(body.getChildText(key)));
                        break;
                    case "Integer":
                    case "int":
                        prod.setValue(key, Integer.parseInt(body.getChildText(key)));
                        break;
                    case "Boolean":
                    case "boolean":
                        prod.setValue(key, Boolean.parseBoolean(body.getChildText(key)));
                        break;
                    case "Date":
                        prod.setValue(key, string2Date(body.getChildText(key)));
                        break;
                    case "LocalDateTime":
                        prod.setValue(key, string2LocalDateTime(body.getChildText(key)));
                        break;
                    default:
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.debug("文件 {} 执行createPojoReflect的 ({}, {}) 时错误，原因：{}", xmlPath, k, v, e.getLocalizedMessage());
            }
        });
    }

    private LocalDateTime string2LocalDateTime(String time) {
        if (StringUtils.isEmpty(time)) {
            return null;
        }
        try {
            if (StringUtils.contains(time, 'T')) {
                time = time.replace('T', ' ');
            }
            return LocalDateTime.parse(time, TIME_FORMAT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Date string2Date(String time) {
        return new Date();
    }
}

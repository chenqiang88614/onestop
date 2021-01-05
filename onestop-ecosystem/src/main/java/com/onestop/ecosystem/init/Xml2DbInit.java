package com.onestop.ecosystem.init;

import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.util.MyStringUtil;
import com.onestop.xml.model.Bean;
import com.onestop.xml.model.Node;
import com.onestop.xml.template.BeanTemplateFactory;
import com.onestop.xml.valid.XmlConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 初始化时将需要生成的XML文件的字段缓存到redis中
 * @author: chenq
 * @date: 2019/9/6 9:43
 */
@Component
@Slf4j
@Order(1)
public class Xml2DbInit implements CommandLineRunner {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public void run(String... args) throws Exception {
        // FIXME 这里只考虑在文件夹中的处理，未考虑xml文件在最终的jar包中的情况
        String xmlPath = XmlConfig.configPath;
        File folder = new File(xmlPath);
        File[] xmlFileList = folder.listFiles();
        Set<File> fileSet = Arrays.stream(Objects.requireNonNull(xmlFileList))
                .filter(file -> StringUtils.endsWith(file.getName(), ".xsd"))
                .collect(Collectors.toCollection(() -> new HashSet<>(xmlFileList.length)));
        doHandler(fileSet);
    }

    private void doHandler(Set<File> fileSet) {
        fileSet.forEach(file -> {
            try {
                String messageType = StringUtils.split(file.getName(), ".")[0];
                Map<String, Node> map = BeanTemplateFactory.createIBean(messageType);
                Node bean = map.get(messageType);
                Node body = bean.getNodeValue("FileBody");
                List<Bean> tagList = body.getData();
                Map<String, String> mapping = new HashMap<>(32);
                tagList.forEach(tag -> {
                    String tagName = MyStringUtil.fisrt2UpperOrLower(tag.getName(), false);
                    if (StringUtils.endsWith(tagName, "ID")) {
                        tagName = tagName.substring(0, tagName.length() - 2) + "Id";
                    }
                    mapping.put(tagName, tag.getName());
                });
                redisTemplate.opsForHash().putAll(RedisKey.XML_TO_DB_PREX.getKey() + messageType, mapping);
            } catch (Exception e) {
                e.printStackTrace();
                log.warn("该文件非正常xml文件，{}", file.getName());
            }
        });
    }
}

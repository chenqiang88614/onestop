package com.onestop.common.conf;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author chenq
 * @description dozer转换自定义配置，具体配置在dozerConverter.xml中
 * @date 2019/7/3 10:54
 */
@Configuration
public class DozerConfig {

    @Bean
    public DozerBeanMapper dozerBeanMapper() {
        try {
            File file = new File("dozerConverter.xml");
            System.out.println("dozerConverter.xml is " + file.exists());
            System.out.println(file.getAbsolutePath());
        } catch (Exception e) {
            System.out.println("cannot open");
        }
        List<String> mappingUrl = Arrays.asList("dozerConverter.xml");
        DozerBeanMapper beanMapper = new DozerBeanMapper();
        beanMapper.setMappingFiles(mappingUrl);
        System.out.println("dozer!!!!");
        return beanMapper;
    }
}

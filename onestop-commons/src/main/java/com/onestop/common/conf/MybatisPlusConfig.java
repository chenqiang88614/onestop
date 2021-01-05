package com.onestop.common.conf;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;

/**
 * @Describe mybatisPlus配置，同时添加了用于分页缓存的相关配置
 * @author chen
 */
@EnableTransactionManagement
@Configuration
public class MybatisPlusConfig {

    /***
     * plus 的性能优化
     * @return
     */
    @Bean
    @Profile("dev")
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        /*<!-- SQL 执行性能分析，开发环境使用，线上不推荐。 maxTime 指的是 sql 最大执行时长 -->*/
        performanceInterceptor.setMaxTime(1000);
        /*<!--SQL是否格式化 默认false-->*/
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType(DbType.POSTGRE_SQL.getDb());
        return page;
    }

    @Bean
    public ISqlInjector logicSqlInjector() {
        return new LogicSqlInjector();
    }

    @Component
    class MetaObjectHandlerConfig implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
//			Date now = new Date();
            LocalDateTime now = LocalDateTime.now();
            setFieldValByName("createTime", now, metaObject);
            setFieldValByName("updateTime", now, metaObject);
            setFieldValByName("deleted", false, metaObject);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
//			Date now = new Date();
            LocalDateTime now = LocalDateTime.now();
            setFieldValByName("updateTime", now, metaObject);
        }
    }
}

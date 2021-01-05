package com.onestop.hbase.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.util.Map;

/**
 * @Author YaoQi
 * @Date 2018/8/13 11:32
 * @Modified
 * @Description HBase配置类
 */
@org.springframework.context.annotation.Configuration
@Slf4j
@Profile("dev")
public class HBaseConfig {

    @Value("${HBase.conf.quorum}")
    private String quorum;

    @Value("${HBase.conf.znodeParent}")
    private String znodeParent;
    private Map<String, String> config;

    @Bean
    public Configuration configuration() {
        Configuration configuration = HBaseConfiguration.create();
        log.info("zookeeper node : {}", quorum);
        log.info("znodeParent is : {}", znodeParent);
        configuration.set("hbase.zookeeper.quorum", quorum);
        configuration.set("zookeeper.znode.parent", znodeParent);

        // 将config中的配置加入到configuration中
        if (config != null && !config.isEmpty()) {
            config.forEach(configuration::set);
        }
        return configuration;
    }

    @Bean
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = ConnectionFactory.createConnection(configuration());
        } catch (IOException e) {
            log.info("get baseAdmin exception {}", e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    @Bean
    public HBaseAdmin getHBaseAdmin() {
        try {
            Connection connection = getConnection();
            return (HBaseAdmin) connection.getAdmin();
        } catch (IOException e) {
            log.info("get baseAdmin exception {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getQuorum() {
        return quorum;
    }

    public void setQuorum(String quorum) {
        this.quorum = quorum;
    }

    public String getZnodeParent() {
        return znodeParent;
    }

    public void setZnodeParent(String znodeParent) {
        this.znodeParent = znodeParent;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }
}

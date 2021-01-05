package com.onestop.common.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenq
 * @description
 * @date 2019/6/27 17:05
 */
@Component
@Slf4j
@Profile("dev")
public class KafkaConsumer {
    @KafkaListener(containerFactory = "kafkaListenerContainerFactory",
            topicPartitions = {@TopicPartition(topic = "testPartition", partitions = {"0"})})
    public void listener1(List<ConsumerRecord<?,?>> records, Acknowledgment ack) {
        log.info("listener1 = {}", System.currentTimeMillis());
        String data = (String)records.get(0).value();
        log.info("data1     = {} ", data);
        List<String> userList = new ArrayList<>();
        try {
            records.forEach(System.out::println);
        } catch (Exception e) {
            log.error("Kafka监听异常"+e.getMessage(),e);
        } finally {
            ack.acknowledge();//手动提交偏移量
        }
    }

    @KafkaListener(containerFactory = "kafkaListenerContainerFactory",
            topicPartitions = {@TopicPartition(topic = "testPartition", partitions = {"1"})})
    public void listener2(List<ConsumerRecord<?,?>> records, Acknowledgment ack) {
        log.info("listener2 = {}", System.currentTimeMillis());
        String data = (String)records.get(0).value();
        log.info("data2     = {}", data);
        List<String> userList = new ArrayList<>();
        try {
            records.forEach(System.out::println);
        } catch (Exception e) {
            log.error("Kafka监听异常"+e.getMessage(),e);
        } finally {
            ack.acknowledge();//手动提交偏移量
        }
    }
}

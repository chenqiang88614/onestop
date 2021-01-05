package com.onestop.common.kafka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.concurrent.ListenableFuture;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenq
 * @description
 * @date 2019/6/27 14:18
 */
@Service(value = "kafkaSendService")
@Slf4j
public class KafkaSenderServiceImpl implements KafkaSenderService{
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    private Gson gson= new GsonBuilder().create();

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendMessage(String topic, String message){
        sendMessage(topic, null, null, null, message);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendMessage(String topic, String key, String message) {
        sendMessage(topic, null, null, key, message);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendMessage(String topic, Integer partition, String message) {
        sendMessage(topic, partition, null, null, message);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendMessage(String topic, Integer partition, String key, String value) {
        sendMessage(topic, partition, null, key, value);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendMessage(String topic, Integer partition, Long timestamp, String key, String message) {
        List partitions = kafkaTemplate.partitionsFor(topic);
        if (partition != null && partitions.size() < partition) {
            log.error("指定partition错误，topic = {} 的partition = [0,{}]，当前partition = {}", topic, partitions.size() - 1, partition);
            return;
        }
        ListenableFuture<SendResult<String, String>> sender = kafkaTemplate.send(new ProducerRecord<>(topic, partition, timestamp, key,  message));
        sender.addCallback(result -> {log.debug("{}数据发送成功", topic);}, ex -> log.error("数据发送失败!"));
    }

    @Override
    // FIXME
    public void sendMessage(Message message) {

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> void sendObject(String topic, T object) {
        sendMessage(topic, null, null, null, gson.toJson(object));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> void sendObject(String topic, String key, T object) {
        sendObject(topic, null, null, null, object);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> void sendObject(String topic, Integer partition, T object) {
        sendObject(topic, partition, null, null, object);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> void sendObject(String topic, Integer partition, String key, T object) {
        sendObject(topic, partition, null, key, object);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> void sendObject(String topic, Integer partition, Long timestamp, String key, T object) {
        sendMessage(topic, partition, timestamp, key, gson.toJson(object));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendMessageList(String topic, List<String> messages) {
        sendMessageList(topic, null, null, null, messages);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendMessageList(String topic, String key, List<String> messages) {
        sendMessageList(topic, null, null, key, messages);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendMessageList(String topic, Integer partition, List<String> messages) {
        sendMessageList(topic, partition, null, null, messages);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendMessageList(String topic, Integer partition, String key, List<String> messages) {
        sendMessageList(topic, partition, null, key, messages);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendMessageList(String topic, Integer partition, Long timestamp, String key, List<String> messages) {
        for (String message : messages) {
            sendMessage(topic, partition, timestamp, key, message);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> void sendObjectList(String topic, List<T> objects) {
        sendObjectList(topic, null, null, null, objects);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> void sendObjectList(String topic, String key, List<T> objects) {
        sendObjectList(topic, null, null, key, objects);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> void sendObjectList(String topic, Integer partition, List<T> objects) {
        sendObjectList(topic,partition, null, null, objects);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> void sendObjectList(String topic, Integer partition, String key, List<T> objects) {
        sendObjectList(topic, partition, null, key, objects);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public <T> void sendObjectList(String topic, Integer partition, Long timestamp, String key, List<T> objects) {
        for (T object : objects) {
            sendMessage(topic, partition, timestamp, key, gson.toJson(object));
        }
    }


}

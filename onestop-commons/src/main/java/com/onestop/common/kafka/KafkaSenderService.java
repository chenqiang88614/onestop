package com.onestop.common.kafka;

import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;

import java.util.List;

/**
 * @author chenq
 * @description
 * @date 2019/7/1 8:59
 */
public interface KafkaSenderService {
    void sendMessage(String topic, String message);

    void sendMessage(String topic, String key, @Nullable String message);

    void sendMessage(String topic, Integer partition, @Nullable String message);

    void sendMessage(String topic, Integer partition, String key, @Nullable String value);

    void sendMessage(String topic, Integer partition, Long timestamp, String key, @Nullable String data);

    void sendMessage(Message message);

    <T> void sendObject(String topic, T object);

    <T> void sendObject(String topic, String key, @Nullable T object);

    <T> void sendObject(String topic, Integer partition, @Nullable T object);

    <T> void sendObject(String topic, Integer partition, String key, @Nullable T object);

    <T> void sendObject(String topic, Integer partition, Long timestamp, String key, @Nullable T object);

    void sendMessageList(String topic, List<String> messages);

    void sendMessageList(String topic, String key, @Nullable List<String> messages);

    void sendMessageList(String topic, Integer partition, @Nullable List<String> messages);

    void sendMessageList(String topic, Integer partition, String key, @Nullable List<String> messages);

    void sendMessageList(String topic, Integer partition, Long timestamp, String key, @Nullable List<String> messages);

    <T> void sendObjectList(String topic, List<T> objects);

    <T> void sendObjectList(String topic, String key, @Nullable List<T> objects);

    <T> void sendObjectList(String topic, Integer partition, @Nullable List<T> objects);

    <T> void sendObjectList(String topic, Integer partition, String key, @Nullable List<T> objects);

    <T> void sendObjectList(String topic, Integer partition, Long timestamp, String key, @Nullable List<T> objects);
}

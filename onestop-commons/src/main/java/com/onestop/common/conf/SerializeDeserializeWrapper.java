package com.onestop.common.conf;

import lombok.Getter;
import lombok.Setter;

/**
 * 序列化/反序列化对象包装类
 * 比如Map、List、String、Enum等是不能进行正确的序列化/反序列化。
 * 因此需要映入一个包装类，把这些需要序列化/反序列化的对象放到这个包装类中。
 */
@Getter
@Setter
public class SerializeDeserializeWrapper<T> {

    private T data;

    public static <T> SerializeDeserializeWrapper<T> builder(T data) {
        SerializeDeserializeWrapper<T> wrapper = new SerializeDeserializeWrapper<>();
        wrapper.setData(data);
        return wrapper;
    }
}
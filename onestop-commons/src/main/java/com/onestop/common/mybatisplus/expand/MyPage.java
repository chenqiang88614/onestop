package com.onestop.common.mybatisplus.expand;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class MyPage<T> implements Serializable {
    private static final long serialVersionUID = 8545996863226528799L;

/*    public Class<T> getTClass() {
        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }*/


    private List<T> records;
    private Long total;

    public MyPage() {
    }

    public MyPage(List<T> records, Long total) {
        this.records = records;
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}


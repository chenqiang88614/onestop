package com.onestop.ecosystem.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public interface ISequenceFactory {

    void set(String key, int value, Date expireTime);

    void set(String key, int value, long timeout, TimeUnit unit);

    long generate(String key, Date expireTime);

    long generate(String key);

    long generate(String key, int increment);

    long generate(String key, int increment, Date expireTime);
}

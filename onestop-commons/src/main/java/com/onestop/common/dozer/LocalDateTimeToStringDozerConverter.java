package com.onestop.common.dozer;

import org.dozer.DozerConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author chenq
 * @description
 * @date 2019/7/3 13:31
 */
public class LocalDateTimeToStringDozerConverter extends DozerConverter<LocalDateTime, String> {
    /**
     * 默认日期时间格式
     */
    private static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public LocalDateTimeToStringDozerConverter() {
        super(LocalDateTime.class, String.class);
    }

    public LocalDateTimeToStringDozerConverter(Class<LocalDateTime> prototypeA, Class<String> prototypeB) {
        super(prototypeA, prototypeB);
    }

    @Override
    public String convertTo(LocalDateTime localDateTime, String s) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.format(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
    }

    @Override
    public LocalDateTime convertFrom(String s, LocalDateTime localDateTime) {
        if (s == null) {
            return null;
        }
        return LocalDateTime.parse(s, DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT));
    }
}

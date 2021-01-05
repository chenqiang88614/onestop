package com.onestop.common.dozer;

import org.dozer.DozerConverter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author chenq
 * @description
 * @date 2019/6/14 14:12
 */
public class LocalDateTimeToDateDozerConverter extends DozerConverter<LocalDateTime, Date> {

    public LocalDateTimeToDateDozerConverter() {
        super(LocalDateTime.class, Date.class);
    }

    public LocalDateTimeToDateDozerConverter(Class<LocalDateTime> prototypeA, Class<Date> prototypeB) {
        super(prototypeA, prototypeB);
    }

    @Override
    public Date convertTo(LocalDateTime localDateTime, Date date) {
        if (localDateTime == null) {
            return null;
        }
        Date convertToDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return convertToDate;
    }

    @Override
    public LocalDateTime convertFrom(Date date, LocalDateTime localDateTime) {
        if (date == null) {
            return null;
        }
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return dateTime;
    }
}

package com.onestop.common.service.impl;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import com.onestop.common.bean.dto.LoggerMessage;
import com.onestop.common.util.LoggerQueue;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.Date;

/**
 * @description: 自定义的日志过滤器
 * @author: chenq
 * @date: 2019/10/14 14:45
 */
@Service
public class LogFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent iLoggingEvent) {
        String execption = "";
        IThrowableProxy iThrowableProxy = iLoggingEvent.getThrowableProxy();
        if (iThrowableProxy != null) {
            execption =
                    "<span class='excehtext'>" + iThrowableProxy.getClassName() + " " + iThrowableProxy.getMessage() + "</span></br>";
            for (int i = 0; i < iThrowableProxy.getStackTraceElementProxyArray().length; i++) {
                execption += "<span class='excetext'>" + iThrowableProxy.getStackTraceElementProxyArray()[i].toString() + "</span></br>";
            }
        }
        LoggerMessage loggerMessage = new LoggerMessage(
                iLoggingEvent.getMessage(),
                DateFormat.getDateInstance().format(new Date(iLoggingEvent.getTimeStamp())),
                iLoggingEvent.getThreadName(),
                iLoggingEvent.getLoggerName(),
                iLoggingEvent.getLevel().levelStr,
                execption,
                ""
        );
        LoggerQueue.getInstance().push(loggerMessage);
        return FilterReply.ACCEPT;
    }
}

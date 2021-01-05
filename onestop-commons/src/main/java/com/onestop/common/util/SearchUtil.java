package com.onestop.common.util;

import com.onestop.common.enums.SystemConst;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenq
 * @description
 * @date 2019/7/8 18:04
 */
public class SearchUtil {
    private static final DateTimeFormatter PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public synchronized static List<LocalDateTime> findByTime(HttpServletRequest request) {
        // 时间范围查询
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");

        if (StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)) {
            return null;
        } else {
            List<LocalDateTime> localDateTimes = new ArrayList<>(2);
            if (StringUtils.isEmpty(startTime)) {
                startTime = SystemConst.DEFAULT_START_TIME;
            }
            LocalDateTime start = LocalDateTime.parse(startTime, PATTERN);
            localDateTimes.add(start);
            if (StringUtils.isEmpty(endTime)) {
                endTime = SystemConst.DEFAULT_END_TIME;
            }
            LocalDateTime end = LocalDateTime.parse(endTime, PATTERN);
            localDateTimes.add(end);
            return localDateTimes;
        }
    }

}

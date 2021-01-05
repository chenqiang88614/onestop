package com.onestop.dao.mapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class SatellitedataMapperProvider {
    private static final String TABLE_NAME = "satellitedata";

    /**
     * 分页查询
     * @param parameter
     * @return
     */
    public String querySatelliteListPaging(Map<String, Object> parameter) {
        return new SQL(){{
            SELECT("s.id, s.imagedate,s.sensorid,s.satelliteid,s.datapath,s.filename");
            FROM(TABLE_NAME + " s");
            Object startTime = parameter.get("startTime");
            if(startTime != null && StringUtils.isNotEmpty(startTime.toString())){
                WHERE("s.imagedate>#{startTime} and s.imagedate<=#{endTime}");
            }
            ORDER_BY("s.imagedate desc limit #{pageSize} offset #{offset} ");
        }}.toString();
    }
    public String countSatellites(Map<String, Object> parameter){
        return new SQL(){{
            SELECT("count(0)");
            FROM(TABLE_NAME + " s");
            Object startTime = parameter.get("startTime");
            if(startTime != null && StringUtils.isNotEmpty(startTime.toString())){
                WHERE("s.imagedate>#{startTime} and s.imagedate<=#{endTime}");
            }
        }}.toString();
    }
    public String queryById(Map<String, Object> parameter) {
        return new SQL() {
            {
                SELECT("s.id, s.imagedate,s.sensorid,s.satelliteid,s.datapath,s.filename");
                FROM(TABLE_NAME + " s");
                WHERE(" s.id = #{id}");
                ORDER_BY("s.imagedate desc");
            }
        }.toString();
    }
}

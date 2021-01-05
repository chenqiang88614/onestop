package com.onestop.dao.mapper;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import java.util.Map;

public class JobMapperProvider {
    private static final String TABLE_NAME = "job";


    /**
     * 查询所有jobs
     * @param parameter
     * @return
     */
    public String queryAllJobs(Map<String, Object> parameter) {
        return new SQL() {
            {
                SELECT("p.*");
                FROM(TABLE_NAME + " p");
                ORDER_BY("p.start_time desc");
            }
        }.toString();
    }

    /**
     * 分页查询
     * @param parameter
     * @return
     */
    public String queryJobListPaging(Map<String, Object> parameter) {
        return new SQL(){{
            SELECT("p.*");
            FROM(TABLE_NAME + " p");
            Object flow_name = parameter.get("flow_name");
            if(flow_name != null && StringUtils.isNotEmpty(flow_name.toString())){
                WHERE("p.flow_name=#{flow_name}");
            }
            Object status = parameter.get("status");
            if(status != null && (int)status != 0){
                WHERE("p.status=#{status}");
            }
            Object startTime = parameter.get("startTime");
            if(startTime != null && StringUtils.isNotEmpty(startTime.toString())){
                WHERE("p.start_time>#{startTime} and p.start_time<=#{endTime}");
            }
            ORDER_BY("p.start_time desc limit #{pageSize} offset #{offset} ");
        }}.toString();
    }

    /**
     * s
     * @param parameter
     * @return
     */
    public String countJobs(Map<String, Object> parameter){
        return new SQL(){{
            SELECT("count(0)");
            FROM(TABLE_NAME + " p");
            Object flow_name = parameter.get("flow_name");
            if(flow_name != null && StringUtils.isNotEmpty(flow_name.toString())){
                WHERE("p.flow_name=#{flow_name}");
            }
            Object status = parameter.get("status");
            if(status != null && (int)status != 0){
                WHERE("p.status=#{status}");
            }
            Object startTime = parameter.get("startTime");
            if(startTime != null && StringUtils.isNotEmpty(startTime.toString())){
                WHERE("p.start_time>#{startTime} and p.start_time<=#{endTime}");
            }
        }}.toString();
    }
    /**
     * 查询jobs个数
     * @param parameter
     * @return
     */
    public String countAllJobs(Map<String, Object> parameter){
        return new SQL(){{
            SELECT("count(0)");
            FROM(TABLE_NAME);
        }}.toString();
    }

    public String queryByFlowName(Map<String, Object> parameter) {
        return new SQL() {
            {
                SELECT("p.*");
                FROM(TABLE_NAME + " p");
                Object flowName = parameter.get("flowName");
                if(flowName != null && StringUtils.isNotEmpty(flowName.toString())){
                    WHERE( " p.flow_name =  #{flowName}");
                }
                ORDER_BY("p.start_time desc");
            }
        }.toString();
    }

    public String queryByJobName(Map<String, Object> parameter) {
        return new SQL() {
            {
                SELECT("p.*");
                FROM(TABLE_NAME + " p");
                Object jobName = parameter.get("jobName");
                if(jobName != null && StringUtils.isNotEmpty(jobName.toString())) {
                    WHERE(" p.job_name =  #{jobName}");
                }
                ORDER_BY("p.start_time desc");
            }
        }.toString();
    }
    public String queryByJobStatus(Map<String, Object> parameter) {
        return new SQL() {
            {
                SELECT("p.*");
                FROM(TABLE_NAME + " p");
                Object status = parameter.get("status");
                if(status != null && StringUtils.isNumeric(status.toString())){
                    WHERE( " p.status =  #{status}");
                }
                ORDER_BY("p.start_time desc");
            }
        }.toString();
    }
    public String queryById(Map<String, Object> parameter) {
        return new SQL() {
            {
                SELECT("p.*");
                FROM(TABLE_NAME + " p");
                WHERE(" p.id = #{id}");
                ORDER_BY("p.create_tme desc");
            }
        }.toString();
    }

}

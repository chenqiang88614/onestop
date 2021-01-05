package com.onestop.dao.mapper;

import com.onestop.dao.bean.Job;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Mapper
public interface JobMapper {

    @Results(value = {@Result(property = "id", column = "id", id = true, javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "outputfile", column = "outputfile", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "create_time", column = "create_tme", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "create_id", column = "create_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "current_task", column = "current_task", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "description", column = "description", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "end_time", column = "end_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "fail_reason", column = "fail_reason", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "flow_name", column = "flow_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "is_plan", column = "is_plan", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "fail_reason", column = "fail_reason", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "job_name", column = "job_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "job_type", column = "job_type", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "parma_info", column = "parma_info", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "plan_start_time", column = "plan_start_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "priority", column = "priority", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "reader_ids", column = "reader_ids", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "result", column = "result", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "start_time", column = "start_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "status", column = "status", javaType = Integer.class, jdbcType = JdbcType.INTEGER)
    })
    @SelectProvider(type = JobMapperProvider.class, method = "queryAllJobs")
    List<Job> getAllJobInfo();


    @SelectProvider(type = JobMapperProvider.class, method = "countJobs")
    Integer countJobs(
            @Param("flow_name")String flowName,
            @Param("status")Integer status,
            @Param("startTime") Date startTime,
            @Param("endTime")Date endTime
    );

    @Results(value = {@Result(property = "id", column = "id", id = true, javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "outputfile", column = "outputfile", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "create_time", column = "create_tme", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "create_id", column = "create_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "current_task", column = "current_task", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "description", column = "description", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "end_time", column = "end_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "fail_reason", column = "fail_reason", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "flow_name", column = "flow_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "is_plan", column = "is_plan", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "fail_reason", column = "fail_reason", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "job_name", column = "job_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "job_type", column = "job_type", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "parma_info", column = "parma_info", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "plan_start_time", column = "plan_start_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "priority", column = "priority", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "reader_ids", column = "reader_ids", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "result", column = "result", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "start_time", column = "start_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "status", column = "status", javaType = Integer.class, jdbcType = JdbcType.INTEGER)
    })
    @SelectProvider(type = JobMapperProvider.class, method = "queryJobListPaging")
    List<Job> queryJobListPaging(@Param("flow_name")String flowName,
                                 @Param("status")Integer status,
                                 @Param("startTime") Date startTime,
                                 @Param("endTime")Date endTime,
                                 @Param("offset")Integer offset,
                                 @Param("pageSize") Integer pageSize);

    @Results(value = {@Result(property = "id", column = "id", id = true, javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "outputfile", column = "outputfile", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "create_time", column = "create_tme", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "create_id", column = "create_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "current_task", column = "current_task", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "description", column = "description", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "end_time", column = "end_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "fail_reason", column = "fail_reason", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "flow_name", column = "flow_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "is_plan", column = "is_plan", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "fail_reason", column = "fail_reason", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "job_name", column = "job_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "job_type", column = "job_type", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "parma_info", column = "parma_info", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "plan_start_time", column = "plan_start_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "priority", column = "priority", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "reader_ids", column = "reader_ids", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "result", column = "result", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "start_time", column = "start_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "status", column = "status", javaType = Integer.class, jdbcType = JdbcType.INTEGER)
    })
    @SelectProvider(type = JobMapperProvider.class, method = "queryByFlowName")
    List<Job> getJobInfoByFlowName(@Param("flowName") String flowName);

    @Results(value = {@Result(property = "id", column = "id", id = true, javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "outputfile", column = "outputfile", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "create_time", column = "create_tme", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "create_id", column = "create_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "current_task", column = "current_task", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "description", column = "description", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "end_time", column = "end_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "fail_reason", column = "fail_reason", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "flow_name", column = "flow_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "is_plan", column = "is_plan", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "fail_reason", column = "fail_reason", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "job_name", column = "job_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "job_type", column = "job_type", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "parma_info", column = "parma_info", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "plan_start_time", column = "plan_start_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "priority", column = "priority", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "reader_ids", column = "reader_ids", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "result", column = "result", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "start_time", column = "start_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "status", column = "status", javaType = Integer.class, jdbcType = JdbcType.INTEGER)
    })
    @SelectProvider(type = JobMapperProvider.class, method = "queryByJobName")
    List<Job> getJobInfoByJobName(@Param("jobName") String jobName);

    @Results(value = {@Result(property = "id", column = "id", id = true, javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "outputfile", column = "outputfile", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "create_time", column = "create_tme", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "create_id", column = "create_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "current_task", column = "current_task", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "description", column = "description", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "end_time", column = "end_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "fail_reason", column = "fail_reason", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "flow_name", column = "flow_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "is_plan", column = "is_plan", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "fail_reason", column = "fail_reason", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "job_name", column = "job_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "job_type", column = "job_type", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "parma_info", column = "parma_info", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "plan_start_time", column = "plan_start_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "priority", column = "priority", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "reader_ids", column = "reader_ids", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "result", column = "result", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "start_time", column = "start_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "status", column = "status", javaType = Integer.class, jdbcType = JdbcType.INTEGER)
    })
    @SelectProvider(type = JobMapperProvider.class, method = "queryByJobStatus")
    List<Job> getJobInfoByStatus(@Param("status") Integer status);
    @Results(value = {@Result(property = "id", column = "id", id = true, javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "outputfile", column = "outputfile", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "create_time", column = "create_tme", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "create_id", column = "create_id", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "current_task", column = "current_task", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "description", column = "description", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "end_time", column = "end_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "fail_reason", column = "fail_reason", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "flow_name", column = "flow_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "is_plan", column = "is_plan", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "fail_reason", column = "fail_reason", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "job_name", column = "job_name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "job_type", column = "job_type", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "parma_info", column = "parma_info", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "plan_start_time", column = "plan_start_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "priority", column = "priority", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "reader_ids", column = "reader_ids", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "result", column = "result", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "start_time", column = "start_time", javaType = Timestamp.class, jdbcType = JdbcType.TIMESTAMP),
            @Result(property = "status", column = "status", javaType = Integer.class, jdbcType = JdbcType.INTEGER)
    })
    @SelectProvider(type = JobMapperProvider.class, method = "queryById")
    Job getJobInfoById(@Param("id") Integer id);
}

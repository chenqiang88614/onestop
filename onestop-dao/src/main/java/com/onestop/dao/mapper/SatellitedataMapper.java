package com.onestop.dao.mapper;

import com.onestop.dao.bean.Satellitedata;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface SatellitedataMapper {

    /**
     *  分页条件查询
     * @param satelliteid 卫星id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param offset 开始
     * @param pageSize 结束
     * @return satellite数据
     */
    @Results(value = {@Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "imagedate", column = "imagedate", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "satelliteid", column = "satelliteid", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "sensorid", column = "sensorid", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "filename", column = "filename", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "datapath", column = "datapath", javaType = String.class, jdbcType = JdbcType.VARCHAR)
    })
    @SelectProvider(type = SatellitedataMapperProvider.class, method = "querySatelliteListPaging")
    List<Satellitedata> queryJobListPaging(@Param("satelliteid")String satelliteid,
                                           @Param("startTime") String startTime,
                                           @Param("endTime")String endTime,
                                           @Param("offset")Integer offset,
                                           @Param("pageSize") Integer pageSize);

    /**
     * 总个数
     * @param satelliteid 卫星id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 总个数
     */
    @SelectProvider(type = SatellitedataMapperProvider.class, method = "countSatellites")
    Integer countSatellites(
            @Param("satelliteid")String satelliteid,
            @Param("startTime") String startTime,
            @Param("endTime")String endTime
    );

    /**
     * 根据id查询satellite
     * @param id
     * @return satellite
     */
    @Results(value = {@Result(property = "id", column = "id", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "imagedate", column = "imagedate", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "satelliteid", column = "satelliteid", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "sensorid", column = "sensorid", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "filename", column = "filename", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "datapath", column = "datapath", javaType = String.class, jdbcType = JdbcType.VARCHAR)
    })
    @SelectProvider(type = SatellitedataMapperProvider.class, method = "queryById")
    Satellitedata querySatelliteById(@Param("id") String id);
}

package com.onestop.dao.mapper;

import com.onestop.dao.bean.ActReModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface ActReModelMapper {
    @Results(value = {@Result(property = "id", column = "id_", id = true, javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "rev", column = "rev_", javaType = Integer.class, jdbcType = JdbcType.INTEGER),
            @Result(property = "name", column = "name_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "key", column = "key_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "category", column = "category_", javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "version", column = "version_", javaType = Integer.class, jdbcType = JdbcType.INTEGER)
    })
    @SelectProvider(type = ActReModelMapperProvider.class, method = "queryAllModel")
    List<ActReModel> getAllJobInfo();
}

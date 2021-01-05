package com.onestop.ecosystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onestop.common.conf.cache.MybatisRedisCache;
import com.onestop.ecosystem.entity.BiologicalAbundance;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface BiologicalAbundanceMapper extends BaseMapper<BiologicalAbundance> {
}
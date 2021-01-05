package com.onestop.ecosystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.onestop.common.conf.cache.MybatisRedisCache;
import com.onestop.ecosystem.entity.EcologicalIndex;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@CacheNamespace(implementation = MybatisRedisCache.class, eviction = MybatisRedisCache.class)
public interface EcologicalIndexMapper extends BaseMapper<EcologicalIndex> {
}
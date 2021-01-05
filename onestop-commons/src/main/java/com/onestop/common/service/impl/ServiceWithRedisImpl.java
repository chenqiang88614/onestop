
package com.onestop.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.onestop.common.conf.SerializeDeserializeWrapper;
import com.onestop.common.mybatisplus.expand.MyLambdaQueryWrapper;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.common.util.CommonUtil;
import com.onestop.common.util.ProtostuffUtils;
import com.onestop.common.util.RedisLocalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;


/**
 * 整合Redis缓存
 */
public class ServiceWithRedisImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> {

    private Class<T> EntityClass; // 获取实体类
    private final Long ENTITY_CACHE_TIME = 60 * 60 * 24 * 7L;//一周
    private final Long PAGE_CACHE_TIME = 600L * 10;
    private final String defaultDimension = ":all";
    private final Byte DQL = (byte) 1;
    private final Byte DML = (byte) 2;

    @Autowired
    private RedisBaseService4Protostuff redisService4Protostuff;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @SuppressWarnings("unchecked")
    public ServiceWithRedisImpl() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        EntityClass = (Class<T>) type.getActualTypeArguments()[1];
    }

    private String getEntityName() {
        return "CACHE:" + EntityClass.getSimpleName() + ":";
    }

    private Serializable getSerializableId(T entity) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(EntityClass);
        if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
            Object idVal = ReflectionKit.getMethodValue(EntityClass, entity, tableInfo.getKeyProperty());
            if (!StringUtils.checkValNull(idVal)) {
                return (Serializable) idVal;
            }
        }
        return null;
    }

    private void set2Redis4Obj(T entity, Byte type) {
        if (entity != null) {
            String key = getEntityName() + getSerializableId(entity).toString();
            boolean isUseTran = DML.equals(type) ? true : false;
            byte[] serialize = ProtostuffUtils.serialize(entity);
            redisService4Protostuff.set(key, serialize, ENTITY_CACHE_TIME, isUseTran);
            if (RedisLocalData.isInTran()){
                RedisLocalData.setCache(key, serialize);
            }
        }
    }

    public void delAllPageCache() {
        redisService4Protostuff.del(getPageHsetKey() + defaultDimension, false);
    }

    public void delDimensionPageCache(String dimension) {
        redisService4Protostuff.del(getSomePageKey(dimension), false);
    }

    /**
     * @param dimension 例如 subs 订单表, 某用户的订单列表, 应该key是 :uid:10001 格式,当用户下了一个订单后,把该用户的分页缓存删除,而不是删除整个subs表的缓存,因为这样做会导致所有用户都要重新查MySQL
     * @return
     */
    private String getSomePageKey(String dimension) {
        return getPageHsetKey() + dimension;
    }

    private String getPageHsetKey() {
        return getEntityName() + "Page";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(T entity) {
        boolean result = super.save(entity);
        if (result) {
            set2Redis4Obj(entity,DML);
            delAllPageCache();
        }
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(Collection<T> entityList) {
        boolean result = super.saveBatch(entityList);
        if (result) {
            entityList.forEach(entity -> set2Redis4Obj(entity,DML));
            delAllPageCache();
        }
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        boolean result = super.saveBatch(entityList, batchSize);
        if (result) {
            entityList.forEach(entity -> set2Redis4Obj(entity,DML));
            delAllPageCache();
        }
        return result;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveOrUpdate(T entity) {
        boolean result = super.saveOrUpdate(entity);
        if (result) {
            set2Redis4Obj(entity,DML);
            delAllPageCache();
        }
        return result;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveOrUpdateBatch(Collection<T> entityList) {
        boolean result = super.saveOrUpdateBatch(entityList);
        if (result) {
            entityList.forEach(entity -> set2Redis4Obj(entity,DML));
            delAllPageCache();
        }
        return result;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        boolean result = super.saveOrUpdateBatch(entityList, batchSize);
        if (result) {
            entityList.forEach(entity -> set2Redis4Obj(entity,DML));
            delAllPageCache();
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        boolean result = super.removeById(id);
        if (result) {
            String key = getEntityName() + id.toString();
            redisService4Protostuff.del(key,false);
            delAllPageCache();
            if (RedisLocalData.isInTran()){
                RedisLocalData.setIsInvalidCache(key);
            }
        }
        return result;
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean removeByMap(Map<String, Object> columnMap) {
        throw new MybatisPlusException("Unsupport method deleteByMap(Map<String, Object> columnMap) for cache !");
    }


    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean remove(Wrapper<T> wrapper) {
        throw new MybatisPlusException("Unsupport method delete(Wrapper<T> wrapper) for cache !");
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        boolean result = super.removeByIds(idList);
        if (result) {
            String entityName = getEntityName();
            boolean inTran = RedisLocalData.isInTran();
            idList.forEach(id -> {
                String key = entityName + id.toString();
                redisService4Protostuff.del(key,false);
                if (inTran){
                    RedisLocalData.setIsInvalidCache(key);
                }
            });
            delAllPageCache();
        }
        return result;
    }

    public T getById(Serializable id, boolean isUseCache) {
        if (!isUseCache) {
            return super.getById(id);
        }
        return getById(id);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public T getById(Serializable id) {
        String key = getEntityName() + id.toString();
        Object obj = RedisLocalData.getObject4Cache(key);
        if (obj != null) {
            //对象在同一事务中, 被新增/更新的情况
            return cast2Entity((byte[]) obj);
        }
        if (RedisLocalData.isDeleted(key)){
            //对象在同一事务中, 被删除的情况
            return null;
        }
        obj = redisService4Protostuff.get(key);
        if (obj != null) {
            return cast2Entity((byte[]) obj);
        }
        T entity = super.getById(id);
        set2Redis4Obj(entity,DML);
        return entity;
    }

    private T cast2Entity(byte[] obj) {
        return EntityClass.cast(ProtostuffUtils.deserialize(obj, EntityClass));
    }

    public MyPage<T> pageEntity(T entity, Page<T> page, boolean isUseCache) {
        return pageEntity(entity, page, isUseCache, defaultDimension, PAGE_CACHE_TIME);
    }

    public MyPage<T> pageEntity(T entity, Page<T> page, boolean isUseCache, String dimension) {
        return pageEntity(entity, page, isUseCache, dimension, PAGE_CACHE_TIME);
    }

    public MyPage<T> pageEntity(T entity, Page<T> page, boolean isUseCache, String dimension, Long expireSeconds) {
//        Page<T> pageInfo = new Page<>(page, size);
        QueryWrapper<T> wrapper = new QueryWrapper<>(entity);
        IPage<T> iPage;

        if (!judgeSteelUseCache(isUseCache, dimension)) {
            iPage = super.page(page, wrapper);
            return new MyPage<>(iPage.getRecords(), iPage.getTotal());
        }

        String hsetKey = getPageHsetKey() + dimension;
        String key = CommonUtil.getMD5x16Str4Entity(entity, false) + ":" + page.getCurrent() + ":" + page.getSize()
                + ":" + Arrays.toString(page.descs()) + ":" + Arrays.toString(page.ascs());
        Object obj = redisService4Protostuff.hget(hsetKey, key);
        if (obj != null) {
            return (MyPage<T>) ProtostuffUtils.deserialize((byte[]) obj, SerializeDeserializeWrapper.class).getData();
        }
        iPage = super.page(page, wrapper);
        MyPage<T> myPage = new MyPage<>(iPage.getRecords(), iPage.getTotal());
        if (iPage.getTotal() > 0) {
            SerializeDeserializeWrapper sdw = SerializeDeserializeWrapper.builder(myPage);
            redisService4Protostuff.hset(hsetKey, key, ProtostuffUtils.serialize(sdw), expireSeconds, true);
        }
        return myPage;
    }

    public MyPage<T> page(MyLambdaQueryWrapper<T> wrapper, Page<T> page, boolean isUseCache, String dimension, Long expireSeconds) {
        isUseCache = wrapper.isSqlRecordListValid() ? isUseCache : false;
//        Page<T> pageInfo = new Page<>(page, size);
        IPage<T> iPage;
        Gson gson = new Gson();
        if (!judgeSteelUseCache(isUseCache, dimension)) {
            iPage = super.page(page, wrapper);
            return new MyPage<>(iPage.getRecords(), iPage.getTotal());
        }

        String hsetKey = getPageHsetKey() + dimension;
        String key = CommonUtil.getMD5x16Str4Collection(wrapper.getSqlRecordList()) + ":" + page.getCurrent() + ":" + page.getSize()
                + ":" + Arrays.toString(page.descs()) + ":" + Arrays.toString(page.ascs());
        Object obj = redisService4Protostuff.hget(hsetKey, key);
        if (obj != null) {
            return (MyPage<T>) ProtostuffUtils.deserialize((byte[]) obj, SerializeDeserializeWrapper.class).getData();
        }
        iPage = super.page(page, wrapper);
        MyPage<T> myPage = new MyPage<>(iPage.getRecords(), iPage.getTotal());
        if (iPage.getTotal() > 0) {
            SerializeDeserializeWrapper sdw = SerializeDeserializeWrapper.builder(myPage);
            redisService4Protostuff.hset(hsetKey, key, ProtostuffUtils.serialize(sdw), expireSeconds, true);
        }
        return myPage;
    }

    private boolean judgeSteelUseCache(boolean isUseCache, String dimension) {
        if (isUseCache && !defaultDimension.equals(dimension) && RedisLocalData.isModified(getEntityName())){
            isUseCache = false;
        }
        return isUseCache;
    }

    public MyPage<T> page(MyLambdaQueryWrapper<T> wrapper, Page<T> page, boolean isUseCache) {
        return page(wrapper, page, isUseCache, defaultDimension, PAGE_CACHE_TIME);
    }

    public MyPage<T> page(MyLambdaQueryWrapper<T> wrapper, Page<T> page, boolean isUseCache, String dimension) {
        return page(wrapper, page, isUseCache, dimension, PAGE_CACHE_TIME);
    }

    public MyPage<T> page(Page<T> page, boolean isUseCache) {
        return page(new MyLambdaQueryWrapper<T>(), page, isUseCache, defaultDimension, PAGE_CACHE_TIME);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(T entity) {
        boolean result = super.updateById(entity);
        if (result) {
            entity = super.getById(getSerializableId(entity));
            set2Redis4Obj(entity,DML);
            delAllPageCache();
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(T entity, Wrapper<T> wrapper) {
        throw new MybatisPlusException("Unsupport method update(T entity, Wrapper<T> wrapper) for cache !");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchById(Collection<T> entityList) {
        boolean result = super.updateBatchById(entityList);
        batchUpdateRedis(entityList, result);
        return result;
    }

    private void batchUpdateRedis(Collection<T> entityList, boolean result) {
        if (result) {
            entityList.forEach(entity -> {
                entity = super.getById(getSerializableId(entity));
                set2Redis4Obj(entity,DML);
            });
            delAllPageCache();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        boolean result = super.updateBatchById(entityList, batchSize);
        batchUpdateRedis(entityList, result);
        return result;
    }

}
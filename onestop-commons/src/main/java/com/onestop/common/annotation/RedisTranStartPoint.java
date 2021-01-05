package com.onestop.common.annotation;


import java.lang.annotation.*;

/**
 * 标记事务的起点,用于解决统一事务中对同一对象进行插入查询更新删除的操作后, 缓存查询不一致的问题
 * 需要在最外层的事务方法上打上该标签
 * @author SRX
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisTranStartPoint {

}

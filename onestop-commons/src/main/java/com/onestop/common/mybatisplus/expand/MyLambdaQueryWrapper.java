
package com.onestop.common.mybatisplus.expand;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class MyLambdaQueryWrapper<T> extends AbstractLambdaWrapper<T, MyLambdaQueryWrapper<T>> implements Query<MyLambdaQueryWrapper<T>, T, SFunction<T, ?>> {
    private String sqlSelect;

    public MyLambdaQueryWrapper() {
        this(null);
    }

    private MyLambdaQueryWrapper(T entity) {
        this.entity = entity;
        this.initEntityClass();
        this.initNeed();
    }

    MyLambdaQueryWrapper(T entity, Class<T> entityClass, String sqlSelect, AtomicInteger paramNameSeq, Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments) {
        this.entity = entity;
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.entityClass = entityClass;
    }

    public List<String> getSqlRecordList() {
        return sqlRecordList;
    }

    public Boolean isSqlRecordListValid() {
        return sqlRecordListIsValid;
    }

    @Override
    @SafeVarargs
    public final MyLambdaQueryWrapper<T> select(SFunction... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.sqlSelect = this.columnsToString(columns);
        }

        return (MyLambdaQueryWrapper) this.typedThis;
    }

    @Override
    public MyLambdaQueryWrapper<T> select(Predicate<TableFieldInfo> predicate) {
        return this.select(this.entityClass, predicate);
    }

    @Override
    public MyLambdaQueryWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        this.entityClass = entityClass;
        this.sqlSelect = TableInfoHelper.getTableInfo(this.getCheckEntityClass()).chooseSelect(predicate);
        return (MyLambdaQueryWrapper) this.typedThis;
    }

    @Override
    public String getSqlSelect() {
        return this.sqlSelect;
    }

    @Override
    public String getCustomSqlSegment() {
        return this.sqlSelect;
    }

    @Override
    protected MyLambdaQueryWrapper<T> instance(AtomicInteger paramNameSeq, Map<String, Object> paramNameValuePairs) {
        return new MyLambdaQueryWrapper(this.entity, this.entityClass, (String) null, paramNameSeq, paramNameValuePairs, new MergeSegments());
    }

}
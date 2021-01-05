package com.onestop.common.util;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;

public class CommonUtil {


    /**
     * 获取一个Bean对象所有非空属性值，MD5成一个字符串，作为Redis的key用。
     *
     * @param t
     * @param bWithSupperFields 是否需要父类
     * @return
     */
    public static <T> String getMD5x16Str4Entity(T t, boolean bWithSupperFields) {
        if (t == null){
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Field fields[] = t.getClass().getDeclaredFields();
        packetField(t, bWithSupperFields, sb, fields, true);
        return SecureUtil.md5X16Str(sb.toString(), "utf-8");
    }

    public static <T> String getMD5x16Str4Collection(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)){
            return "";
        }
        return SecureUtil.md5X16Str(collection.toString(), "utf-8");
    }

    private static <T> void packetField(T t, boolean bWithSupperFields, StringBuffer sb, Field[] fields, boolean hasPageSize) {
        Field[] father;
        try {
            getFieldNames(t, sb, fields, hasPageSize);
            if (bWithSupperFields) {
                father = t.getClass().getSuperclass().getDeclaredFields();
                getFieldNames(t, sb, father, hasPageSize);
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private static <T> void getFieldNames(T t, StringBuffer sb, Field[] father, boolean hasPageSize) throws IllegalAccessException {
        Field field;
        for (int i = 0; i < father.length; i++) {
            field = father[i];
            // 这两个字段不参与计算
            if ("page".equals(field.getName())) {
                continue;
            }
            if (hasPageSize && "pageSize".equals(field.getName())) {
                continue;
            }

            field.setAccessible(true);
            Object val = field.get(t);
            if (val != null) {
                sb.append(field.getName()).append(":").append(val);
            }
        }
    }
}

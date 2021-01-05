package com.onestop.ecosystem.init;

import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.util.ScannerPackage;
import org.apache.commons.lang3.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Model2DbInit.class)
public class Model2DbInitTest {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @org.junit.Test
    public void run() {
        Set<Class<?>> classes = ScannerPackage.getClasses("com.onestop.ecosystem.model");
        classes.forEach(this::model2db);
    }

    private void model2db(Class classz) {
        Field[] fields = classz.getDeclaredFields();
        Map<String, String> map = new HashMap<>(fields.length);
        for (Field field : fields) {
            if (StringUtils.equals(field.getName(), "serialVersionUID")) {
                continue;
            }
            // 如果属性不是private的直接跳过
            if (field.getModifiers() != Modifier.PRIVATE.ordinal()) {
                continue;
            }
            String fieldName = field.getName();
            fieldName = fisrt2Upper(fieldName);
            if (StringUtils.endsWith(fieldName, "Id")) {
                fieldName = fieldName.substring(0, fieldName.length() - 2) + "ID";
            }
            String[] type = StringUtils.split(field.getGenericType().getTypeName(), "\\.");
            map.put(fieldName, type[type.length - 1]);
        }

        String hashkey = RedisKey.MODEL_TO_DB + ":" + classz.getSimpleName().toUpperCase();
        redisTemplate.opsForHash().putAll(hashkey, map);
    }

    private String fisrt2Upper(String word) {
        String s1 = word.substring(0, 1);
        s1 = s1.toUpperCase();
        return s1.concat(word.substring(1));
    }
}
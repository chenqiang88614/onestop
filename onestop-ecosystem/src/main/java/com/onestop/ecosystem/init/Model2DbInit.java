package com.onestop.ecosystem.init;

import com.onestop.ecosystem.constant.PreprocessStatus;
import com.onestop.ecosystem.constant.ProdOrderStatus;
import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.entity.ThematicType;
import com.onestop.ecosystem.service.IThematicTypeService;
import com.onestop.ecosystem.util.MyStringUtil;
import com.onestop.ecosystem.util.ScannerPackage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.lang.model.element.Modifier;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description 初始化时扫描model包下面的所有实体类，将类的属性转换成xml的标签，存储在redis的BASIC:HASH:MODEL2DB字段中
 * @author chenq
 * @date 2019年9月2日18:05:41
 */
@Component
@Slf4j
@Order(2)
public class Model2DbInit implements CommandLineRunner {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private IThematicTypeService thematicTypeService;

    @Override
    public void run(String... args) throws Exception {
        Set<Class<?>> classes = ScannerPackage.getClasses("com.onestop.ecosystem.entity");
        classes.addAll(ScannerPackage.getClasses("com.onestop.ecosystem.bean"));
        classes.forEach(this::model2db);

        // orderStatus TO redis
        // ProdOrderStatus TO Redis
        ProdOrderStatus[] values = ProdOrderStatus.values();
        Map<String, String> map = new HashMap<>(16);
        for (ProdOrderStatus value : values) {
            map.put(String.valueOf(value.getKey()), value.getDesc());
        }
        redisTemplate.opsForHash().putAll(RedisKey.PROD_ORDER_STATUS.getKey(), map);

        // PreprocessStatus To redis
        map.clear();
        PreprocessStatus[] preprocessStatuses = PreprocessStatus.values();
        for (PreprocessStatus preprocessStatus : preprocessStatuses) {
            map.put(String.valueOf(preprocessStatus.getKey()), preprocessStatus.getName());
        }
        redisTemplate.opsForHash().putAll(RedisKey.PRE_PROCESS_ORDER_STATUS.getKey(), map);

        // thematicType To Redis
        List<ThematicType> thematicTypeList = thematicTypeService.list();
        map.clear();
        thematicTypeList.forEach(thematicType -> {
            if (StringUtils.isNotEmpty(thematicType.getTag())) {
                map.put(thematicType.getTag(), thematicType.getNamech());
            }
        });
        redisTemplate.opsForHash().putAll(RedisKey.THEMATIC_TYPE.getKey(), map);
    }

    private void model2db(Class classz) {
        Field[] fields = classz.getDeclaredFields();
        Map<String, String> map = new HashMap<>(fields.length);
        for (Field field : fields) {
            if (StringUtils.equals(field.getName(), "serialVersionUID") || StringUtils.equals(field.getName(), "id")) {
                continue;
            }
            // 如果属性不是private的直接跳过
            if (field.getModifiers() != Modifier.PRIVATE.ordinal()) {
                continue;
            }
            String fieldName = field.getName();
            fieldName = MyStringUtil.fisrt2UpperOrLower(fieldName, true);
            if (StringUtils.endsWith(fieldName, "Id")) {
                fieldName = fieldName.substring(0, fieldName.length() - 2) + "ID";
            }
            String[] type = StringUtils.split(field.getGenericType().getTypeName(), "\\.");
            map.put(fieldName, type[type.length - 1]);
        }

        String hashkey = RedisKey.MODEL_TO_DB.getKey() + ":" + classz.getSimpleName().toUpperCase();
        redisTemplate.opsForHash().putAll(hashkey, map);
    }

}

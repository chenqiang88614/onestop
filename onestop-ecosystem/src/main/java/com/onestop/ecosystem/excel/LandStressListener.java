package com.onestop.ecosystem.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.onestop.common.util.SpringContextHolder;
import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.entity.EcologicalIndex;
import com.onestop.ecosystem.entity.LandStress;
import com.onestop.ecosystem.service.LandStressService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

/**
 * 模板的读取类
 *
 * @author Jiaju Zhuang
 */
@Data
public class LandStressListener extends AnalysisEventListener<LandStress> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LandStressListener.class);

    private RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");

    private LandStressService landStressService = SpringContextHolder.getBean(
            "landStressService");

    private String year;

    public LandStressListener(String year) {
        super();
        this.year = year;
    }
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    List<LandStress> list = new ArrayList<>();

    @Override
    public void invoke(LandStress data, AnalysisContext context) {
//        LOGGER.info(year);
//        LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
        list.add(data);
        if (list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        LOGGER.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        Iterator<LandStress> iterator = list.iterator();
        Map map = new HashMap<>(list.size());
        try{
            map = redisTemplate.opsForHash().entries(RedisKey.ECOLOGICAL_INDEX.getKey());
        } catch (Exception e) {
            LOGGER.warn(e.getLocalizedMessage());
        }
        while (iterator.hasNext()) {
            LandStress landStress = iterator.next();
            if (StringUtils.isEmpty(landStress.getCountry()) ) {
                iterator.remove();
                continue;
            } else if(StringUtils.isEmpty(landStress.getCode())) {
                String code = (String) redisTemplate.opsForHash().get(RedisKey.COUNTRY_CODE_TO_REDIS_PREX.getKey() +
                        "1", landStress.getCountry());
                landStress.setCode(code);
            } else if (!StringUtils.startsWith(landStress.getCode(), "63")){
                iterator.remove();
                continue;
            }
            landStress.setId(landStress.getCode() + "+" + this.year);
            landStress.setYear(this.year);

            EcologicalIndex ecologicalIndex = null;
            if (map.containsKey(landStress.getCode())) {
                ecologicalIndex = (EcologicalIndex) map.get(landStress.getCode());
                ecologicalIndex.setLandStress(landStress.getIndex());
            } else {
                ecologicalIndex = new EcologicalIndex();
                ecologicalIndex.setId(landStress.getId());
                ecologicalIndex.setCountry(landStress.getCountry());
                ecologicalIndex.setCode(landStress.getCode());
                ecologicalIndex.setYear(this.year);
                ecologicalIndex.setLandStress(landStress.getIndex());
            }
            map.put(landStress.getCode(), ecologicalIndex);
        }
        landStressService.saveOrUpdateBatch(list);
        redisTemplate.opsForHash().putAll(RedisKey.ECOLOGICAL_INDEX.getKey(), map);
        LOGGER.info(this.getClass().getSimpleName() + "存储数据库成功！" + list.size());
    }
}

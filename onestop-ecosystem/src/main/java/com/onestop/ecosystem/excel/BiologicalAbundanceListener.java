package com.onestop.ecosystem.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.onestop.common.util.SpringContextHolder;
import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.entity.BiologicalAbundance;
import com.onestop.ecosystem.entity.EcologicalIndex;
import com.onestop.ecosystem.service.BiologicalAbundanceService;
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
public class BiologicalAbundanceListener extends AnalysisEventListener<BiologicalAbundance> {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiologicalAbundanceListener.class);

    private RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");

    private BiologicalAbundanceService biologicalAbundanceService = SpringContextHolder.getBean(
            "biologicalAbundanceService");

    private String year;

    public BiologicalAbundanceListener(String year) {
        super();
        this.year = year;
    }
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    List<BiologicalAbundance> list = new ArrayList<>();

    @Override
    public void invoke(BiologicalAbundance data, AnalysisContext context) {
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
        Iterator<BiologicalAbundance> iterator = list.iterator();
        Map map = new HashMap<>(list.size());
        try{
            map = redisTemplate.opsForHash().entries(RedisKey.ECOLOGICAL_INDEX.getKey());
        } catch (Exception e) {
            LOGGER.warn(e.getLocalizedMessage());
        }
        while (iterator.hasNext()) {
            BiologicalAbundance biologicalAbundance = iterator.next();
            if (StringUtils.isEmpty(biologicalAbundance.getCountry())) {
                iterator.remove();
                continue;
            } else if(StringUtils.isEmpty(biologicalAbundance.getCode())) {
                String code = (String) redisTemplate.opsForHash().get(RedisKey.COUNTRY_CODE_TO_REDIS_PREX.getKey() +
                        "1", biologicalAbundance.getCountry());
                biologicalAbundance.setCode(code);
            } else if (!StringUtils.startsWith(biologicalAbundance.getCode(), "63")) {
                iterator.remove();
                continue;
            }
            biologicalAbundance.setId(biologicalAbundance.getCode() + "+" + this.year);
            biologicalAbundance.setYear(this.year);

            EcologicalIndex ecologicalIndex = null;
            if (map.containsKey(biologicalAbundance.getCode())) {
                ecologicalIndex = (EcologicalIndex) map.get(biologicalAbundance.getCode());
                ecologicalIndex.setBiologicalAbundance(biologicalAbundance.getIndex());
            } else {
                ecologicalIndex = new EcologicalIndex();
                ecologicalIndex.setId(biologicalAbundance.getId());
                ecologicalIndex.setCountry(biologicalAbundance.getCountry());
                ecologicalIndex.setCode(biologicalAbundance.getCode());
                ecologicalIndex.setYear(this.year);
                ecologicalIndex.setBiologicalAbundance(biologicalAbundance.getIndex());
            }
            map.put(biologicalAbundance.getCode(), ecologicalIndex);
        }
        biologicalAbundanceService.saveOrUpdateBatch(list);
        redisTemplate.opsForHash().putAll(RedisKey.ECOLOGICAL_INDEX.getKey(), map);
        LOGGER.info(this.getClass().getSimpleName() + "存储数据库成功！" + list.size());
    }
}

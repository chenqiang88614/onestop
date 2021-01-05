package com.onestop.ecosystem.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.onestop.common.util.SpringContextHolder;
import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.entity.EcologicalIndex;
import com.onestop.ecosystem.entity.VegetationalCover;
import com.onestop.ecosystem.service.VegetationalCoverService;
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
public class VegetationalCoverListener extends AnalysisEventListener<VegetationalCover> {
    private static final Logger LOGGER = LoggerFactory.getLogger(VegetationalCoverListener.class);

    private RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");

    private VegetationalCoverService vegetationalCoverService = SpringContextHolder.getBean(
            "vegetationalCoverService");

    private String year;

    public VegetationalCoverListener(String year) {
        super();
        this.year = year;
    }
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    List<VegetationalCover> list = new ArrayList<>();

    @Override
    public void invoke(VegetationalCover data, AnalysisContext context) {
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
        Iterator<VegetationalCover> iterator = list.iterator();
        Map map = new HashMap<>(list.size());
        try{
            map = redisTemplate.opsForHash().entries(RedisKey.ECOLOGICAL_INDEX.getKey());
        } catch (Exception e) {
            LOGGER.warn(e.getLocalizedMessage());
        }
        while (iterator.hasNext()) {
            VegetationalCover vegetationalCover = iterator.next();
            if (StringUtils.isEmpty(vegetationalCover.getCountry())) {
                iterator.remove();
                continue;
            } else if(StringUtils.isEmpty(vegetationalCover.getCode())) {
                String code = (String) redisTemplate.opsForHash().get(RedisKey.COUNTRY_CODE_TO_REDIS_PREX.getKey() +
                        "1", vegetationalCover.getCountry());
                vegetationalCover.setCode(code);
            } else if (!StringUtils.startsWith(vegetationalCover.getCode(), "63")) {
                iterator.remove();
                continue;
            }
            vegetationalCover.setId(vegetationalCover.getCode() + "+" + this.year);
            vegetationalCover.setYear(this.year);

            EcologicalIndex ecologicalIndex = null;
            if (map.containsKey(vegetationalCover.getCode())) {
                ecologicalIndex = (EcologicalIndex) map.get(vegetationalCover.getCode());
                ecologicalIndex.setVegetationalCover(vegetationalCover.getIndex());
            } else {
                ecologicalIndex = new EcologicalIndex();
                ecologicalIndex.setId(vegetationalCover.getId());
                ecologicalIndex.setCountry(vegetationalCover.getCountry());
                ecologicalIndex.setCode(vegetationalCover.getCode());
                ecologicalIndex.setYear(this.year);
                ecologicalIndex.setVegetationalCover(vegetationalCover.getIndex());
            }
            map.put(vegetationalCover.getCode(), ecologicalIndex);
        }
        vegetationalCoverService.saveOrUpdateBatch(list);
        redisTemplate.opsForHash().putAll(RedisKey.ECOLOGICAL_INDEX.getKey(), map);
        LOGGER.info(this.getClass().getSimpleName() + "存储数据库成功！" + list.size());
    }
}

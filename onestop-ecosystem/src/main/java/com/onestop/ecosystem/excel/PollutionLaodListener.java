package com.onestop.ecosystem.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.onestop.common.util.SpringContextHolder;
import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.entity.EcologicalIndex;
import com.onestop.ecosystem.entity.PollutionLoad;
import com.onestop.ecosystem.service.PollutionLoadService;
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
public class PollutionLaodListener extends AnalysisEventListener<PollutionLoad> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PollutionLaodListener.class);

    private RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");

    private PollutionLoadService pollutionLoadService = SpringContextHolder.getBean(
            "pollutionLoadService");

    private String year;

    public PollutionLaodListener(String year) {
        super();
        this.year = year;
    }
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    List<PollutionLoad> list = new ArrayList<>();

    @Override
    public void invoke(PollutionLoad data, AnalysisContext context) {
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
        Iterator<PollutionLoad> iterator = list.iterator();
        Map map = new HashMap<>(list.size());
        try{
            map = redisTemplate.opsForHash().entries(RedisKey.ECOLOGICAL_INDEX.getKey());
        } catch (Exception e) {
            LOGGER.warn(e.getLocalizedMessage());
        }
        while (iterator.hasNext()) {
            PollutionLoad pollutionLoad = iterator.next();
            if (StringUtils.isEmpty(pollutionLoad.getCountry())) {
                iterator.remove();
                continue;
            } else if(StringUtils.isEmpty(pollutionLoad.getCode())) {
                String code = (String) redisTemplate.opsForHash().get(RedisKey.COUNTRY_CODE_TO_REDIS_PREX.getKey() +
                        "1", pollutionLoad.getCountry());
                pollutionLoad.setCode(code);
            } else if (!StringUtils.startsWith(pollutionLoad.getCode(),"63")) {
                iterator.remove();
                continue;
            }
            pollutionLoad.setId(pollutionLoad.getCode() + "+" + this.year);
            pollutionLoad.setYear(this.year);

            EcologicalIndex ecologicalIndex = null;
            if (map.containsKey(pollutionLoad.getCode())) {
                ecologicalIndex = (EcologicalIndex) map.get(pollutionLoad.getCode());
                ecologicalIndex.setPollutionLoad(pollutionLoad.getIndex());
            } else {
                ecologicalIndex = new EcologicalIndex();
                ecologicalIndex.setId(pollutionLoad.getId());
                ecologicalIndex.setCountry(pollutionLoad.getCountry());
                ecologicalIndex.setCode(pollutionLoad.getCode());
                ecologicalIndex.setYear(this.year);
                ecologicalIndex.setPollutionLoad(pollutionLoad.getIndex());
            }
            map.put(pollutionLoad.getCode(), ecologicalIndex);
        }
        pollutionLoadService.saveOrUpdateBatch(list);
        redisTemplate.opsForHash().putAll(RedisKey.ECOLOGICAL_INDEX.getKey(), map);
        LOGGER.info(this.getClass().getSimpleName() + "存储数据库成功！" + list.size());
    }
}

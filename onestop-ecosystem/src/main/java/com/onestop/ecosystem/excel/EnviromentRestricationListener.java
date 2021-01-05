package com.onestop.ecosystem.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.onestop.common.util.SpringContextHolder;
import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.entity.EcologicalIndex;
import com.onestop.ecosystem.entity.EnviromentRestrication;
import com.onestop.ecosystem.service.EnviromentRestricationService;
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
public class EnviromentRestricationListener extends AnalysisEventListener<EnviromentRestrication> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EnviromentRestricationListener.class);

    private RedisTemplate<String, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");

    private EnviromentRestricationService enviromentRestricationService = SpringContextHolder.getBean(
            "enviromentRestricationService");

    private String year;

    public EnviromentRestricationListener(String year) {
        super();
        this.year = year;
    }
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 3000;
    List<EnviromentRestrication> list = new ArrayList<EnviromentRestrication>();

    @Override
    public void invoke(EnviromentRestrication data, AnalysisContext context) {
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
        Iterator<EnviromentRestrication> iterator = list.iterator();
        Map map = new HashMap<>(list.size());
        try{
            map = redisTemplate.opsForHash().entries(RedisKey.ECOLOGICAL_INDEX.getKey());
        } catch (Exception e) {
            LOGGER.warn(e.getLocalizedMessage());
        }
        while (iterator.hasNext()) {
            EnviromentRestrication enviromentRestrication = iterator.next();
            if (StringUtils.isEmpty(enviromentRestrication.getCountry())) {
                iterator.remove();
                continue;
            } else if(StringUtils.isEmpty(enviromentRestrication.getCode())) {
                String code = (String) redisTemplate.opsForHash().get(RedisKey.COUNTRY_CODE_TO_REDIS_PREX.getKey() +
                        "1", enviromentRestrication.getCountry());
                enviromentRestrication.setCode(code);
            } else if ((!StringUtils.startsWith(enviromentRestrication.getCode(), "63"))) {
                iterator.remove();
                continue;
            }
            enviromentRestrication.setId(enviromentRestrication.getCode() + "+" + this.year);
            enviromentRestrication.setYear(this.year);

            EcologicalIndex ecologicalIndex = null;
            if (map.containsKey(enviromentRestrication.getCode())) {
                ecologicalIndex = (EcologicalIndex) map.get(enviromentRestrication.getCode());
                ecologicalIndex.setEnvironmentRestriction(enviromentRestrication.getIndex());
            } else {
                ecologicalIndex = new EcologicalIndex();
                ecologicalIndex.setId(enviromentRestrication.getId());
                ecologicalIndex.setCountry(enviromentRestrication.getCountry());
                ecologicalIndex.setCode(enviromentRestrication.getCode());
                ecologicalIndex.setYear(this.year);
                ecologicalIndex.setEnvironmentRestriction(enviromentRestrication.getIndex());
            }
            map.put(enviromentRestrication.getCode(), ecologicalIndex);
        }
        enviromentRestricationService.saveOrUpdateBatch(list);
        redisTemplate.opsForHash().putAll(RedisKey.ECOLOGICAL_INDEX.getKey(), map);
        LOGGER.info(this.getClass().getSimpleName() + "存储数据库成功！" + list.size());
    }
}

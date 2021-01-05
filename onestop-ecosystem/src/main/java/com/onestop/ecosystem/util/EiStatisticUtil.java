package com.onestop.ecosystem.util;

import com.onestop.ecosystem.vo.EnviromentRestricationVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @description: EI统计的公共类
 * @author: chenq
 * @date: 2019/9/29 17:09
 */
public class EiStatisticUtil {

    public static void calc(List<T> list, String[] years, Class clazzVo) {
        /*Map<String, Map> map = new HashMap<>();
        AtomicReference<BigDecimal> thisYearTotal = new AtomicReference<>(new BigDecimal(0));
        AtomicReference<BigDecimal> lastYearTotal = new AtomicReference<>(new BigDecimal(0));
        EnviromentRestricationVo thisYearSingle = new EnviromentRestricationVo();
        EnviromentRestricationVo lastYearSingle = new EnviromentRestricationVo();

        AtomicReference<Boolean> flag = new AtomicReference<>(false);

        list.forEach(enviromentRestrication -> {
            null2zero(enviromentRestrication);
            if (StringUtils.equals(years[1], enviromentRestrication.getYear())) {
                // 如果是当前年，统计总数
                this.calcTotal(thisYearTotal, enviromentRestrication);
                this.calcSingle(thisYearSingle, enviromentRestrication);
            } else {
                this.calcTotal(lastYearTotal, enviromentRestrication);
                this.calcSingle(lastYearSingle, enviromentRestrication);
                flag.set(true);
            }
        });

        Map<String, Object> thisEle = new HashMap<>();
        thisEle.put("total", thisYearTotal.get());
        thisEle.put("single", thisYearSingle);
        map.put(years[1], thisEle);
        Map<String, Object> lastEle = new HashMap<>();
        lastEle.put("total", lastYearTotal.get());
        lastEle.put("single", lastYearSingle);
//        map.put(years[0], lastEle);

        Map<String, Object> delta = new HashMap<>();
        if (flag.get()) {
            delta.put("total", thisYearTotal.get().subtract(lastYearTotal.get()));
            EnviromentRestricationVo deltaEnv = new EnviromentRestricationVo();
            deltaEnv.setConstructionLand(thisYearSingle.getConstructionLand().subtract(lastYearSingle.getConstructionLand()));
            deltaEnv.setForest(thisYearSingle.getForest().subtract(lastYearSingle.getForest()));
            deltaEnv.setGrassland(thisYearSingle.getGrassland().subtract(lastYearSingle.getGrassland()));
            deltaEnv.setPlowland(thisYearSingle.getPlowland().subtract(lastYearSingle.getPlowland()));
            deltaEnv.setUnusedLand(thisYearSingle.getUnusedLand().subtract(lastYearSingle.getUnusedLand()));
            deltaEnv.setWetland(thisYearSingle.getWetland().subtract(lastYearSingle.getWetland()));
            delta.put("single", deltaEnv);
        } else {
            delta.put("total", new BigDecimal(0));
            delta.put("single", 0);
        }

        map.put("delta", delta);*/
    }
}

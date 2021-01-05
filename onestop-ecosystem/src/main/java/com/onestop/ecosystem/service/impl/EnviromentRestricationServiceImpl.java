package com.onestop.ecosystem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyLambdaQueryWrapper;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.common.service.impl.ServiceWithRedisImpl;
import com.onestop.common.util.BeanMapper;
import com.onestop.ecosystem.vo.EnviromentRestricationVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.onestop.ecosystem.entity.EnviromentRestrication;
import com.onestop.ecosystem.mapper.EnviromentRestricationMapper;
import com.onestop.ecosystem.service.EnviromentRestricationService;

@Service("enviromentRestricationService")
public class EnviromentRestricationServiceImpl extends ServiceWithRedisImpl<EnviromentRestricationMapper,
        EnviromentRestrication> implements EnviromentRestricationService {
    private static BigDecimal zero = new BigDecimal(0);
    @Override
    public MyPage<EnviromentRestricationVo> list(Page page, HttpServletRequest request) {
        if (page.ascs() == null && page.descs() == null) {
            page.setAsc("code");
        }
        MyLambdaQueryWrapper<EnviromentRestrication> queryWrapper = this.createQueryWrapper(request);
        MyPage<EnviromentRestrication> biologicalAbundanceIPage = this.page(queryWrapper, page, true);
        MyPage<EnviromentRestricationVo> biologicalAbundanceVoMyPage = new MyPage<>();
        biologicalAbundanceVoMyPage.setTotal(biologicalAbundanceIPage.getTotal());
        List<EnviromentRestrication> biologicalAbundanceList = biologicalAbundanceIPage.getRecords();
        List<EnviromentRestricationVo> biologicalAbundanceVoList = BeanMapper.mapList(biologicalAbundanceList,
                EnviromentRestricationVo.class);
        biologicalAbundanceVoMyPage.setRecords(biologicalAbundanceVoList);
        return biologicalAbundanceVoMyPage;
    }

    @Override
    public Map statistic(String[] countryArray, String[] years) {
        Map<String, Map> map = new HashMap<>();
        MyLambdaQueryWrapper<EnviromentRestrication> queryWrapper = new MyLambdaQueryWrapper<>();
        queryWrapper.in(EnviromentRestrication::getCountry, countryArray);
        queryWrapper.in(EnviromentRestrication::getYear, years);
        queryWrapper.orderByDesc(EnviromentRestrication::getYear);
        List<EnviromentRestrication> enviromentRestricationList = this.list(queryWrapper);

        AtomicReference<BigDecimal> thisYearTotal = new AtomicReference<>(new BigDecimal(0));
        AtomicReference<BigDecimal> lastYearTotal = new AtomicReference<>(new BigDecimal(0));
        EnviromentRestricationVo thisYearSingle = new EnviromentRestricationVo();
        EnviromentRestricationVo lastYearSingle = new EnviromentRestricationVo();

        AtomicReference<Boolean> flag = new AtomicReference<>(false);

        enviromentRestricationList.forEach(enviromentRestrication -> {
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

        map.put("delta", delta);
        return map;
    }

    private MyLambdaQueryWrapper<EnviromentRestrication> createQueryWrapper(HttpServletRequest request) {
        MyLambdaQueryWrapper<EnviromentRestrication> queryWrapper = new MyLambdaQueryWrapper<>();
        String year = request.getParameter("year");
        if (StringUtils.isNotEmpty(year)) {
            queryWrapper.eq(EnviromentRestrication::getYear, year);
        }

        return queryWrapper;
    }

    private void calcSingle(EnviromentRestricationVo e1, EnviromentRestrication e2) {
        e1.setConstructionLand(e1.getConstructionLand().add(e2.getConstructionLand()));
        e1.setForest(e1.getForest().add(e2.getForest()));
        e1.setGrassland(e1.getGrassland().add(e2.getGrassland()));
        e1.setPlowland(e1.getPlowland().add(e2.getPlowland()));
        e1.setUnusedLand(e1.getUnusedLand().add(e1.getUnusedLand()));
        e1.setWetland(e1.getWetland().add(e2.getWetland()));
    }

    private void calcTotal(AtomicReference<BigDecimal> total, EnviromentRestrication enviromentRestrication) {
        total.set(total.get().add(enviromentRestrication.getConstructionLand())
                .add(enviromentRestrication.getForest())
                .add(enviromentRestrication.getGrassland())
                .add(enviromentRestrication.getPlowland())
                .add(enviromentRestrication.getConstructionLand())
                .add(enviromentRestrication.getUnusedLand())
                .add(enviromentRestrication.getWetland()));
    }

    private void null2zero(EnviromentRestrication e1) {
        e1.setWetland(e1.getWetland() == null ? zero : e1.getWetland());
        e1.setUnusedLand(e1.getUnusedLand() == null ? zero : e1.getUnusedLand());
        e1.setPlowland(e1.getPlowland() == null ? zero : e1.getPlowland());
        e1.setGrassland(e1.getGrassland() == null ? zero : e1.getGrassland());
        e1.setForest(e1.getForest() == null ? zero : e1.getForest());
        e1.setConstructionLand(e1.getConstructionLand() == null ? zero : e1.getConstructionLand());
    }
}


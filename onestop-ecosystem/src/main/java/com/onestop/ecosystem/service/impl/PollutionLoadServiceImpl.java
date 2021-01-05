package com.onestop.ecosystem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyLambdaQueryWrapper;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.common.service.impl.ServiceWithRedisImpl;
import com.onestop.common.util.BeanMapper;
import com.onestop.ecosystem.entity.PollutionLoad;
import com.onestop.ecosystem.entity.PollutionLoad;
import com.onestop.ecosystem.vo.PollutionLoadVo;
import com.onestop.ecosystem.vo.PollutionLoadVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onestop.ecosystem.entity.PollutionLoad;
import com.onestop.ecosystem.mapper.PollutionLoadMapper;
import com.onestop.ecosystem.service.PollutionLoadService;
@Service("pollutionLoadService")
public class PollutionLoadServiceImpl extends ServiceWithRedisImpl<PollutionLoadMapper, PollutionLoad> implements PollutionLoadService{
    private static BigDecimal zero = new BigDecimal(0);
    @Override
    public MyPage<PollutionLoadVo> list(Page page, HttpServletRequest request) {
        if (page.ascs() == null && page.descs() == null) {
            page.setAsc("code");
        }
        MyLambdaQueryWrapper<PollutionLoad> queryWrapper = this.createQueryWrapper(request);
        MyPage<PollutionLoad> biologicalAbundanceIPage = this.page(queryWrapper, page, true);
        MyPage<PollutionLoadVo> biologicalAbundanceVoMyPage = new MyPage<>();
        biologicalAbundanceVoMyPage.setTotal(biologicalAbundanceIPage.getTotal());
        List<PollutionLoad> biologicalAbundanceList = biologicalAbundanceIPage.getRecords();
        List<PollutionLoadVo> biologicalAbundanceVoList = BeanMapper.mapList(biologicalAbundanceList,
                PollutionLoadVo.class);
        biologicalAbundanceVoMyPage.setRecords(biologicalAbundanceVoList);
        return biologicalAbundanceVoMyPage;
    }

    @Override
    public Map statistic(String[] countryArray, String[] years) {
        Map<String, Map> map = new HashMap<>();
        MyLambdaQueryWrapper<PollutionLoad> queryWrapper = new MyLambdaQueryWrapper<>();
        queryWrapper.in(PollutionLoad::getCountry, countryArray);
        queryWrapper.in(PollutionLoad::getYear, years);
        queryWrapper.orderByDesc(PollutionLoad::getYear);
        List<PollutionLoad> pollutionLoadList = this.list(queryWrapper);

        AtomicReference<BigDecimal> thisYearTotal = new AtomicReference<>(new BigDecimal(0));
        AtomicReference<BigDecimal> lastYearTotal = new AtomicReference<>(new BigDecimal(0));
        PollutionLoadVo thisYearSingle = new PollutionLoadVo();
        PollutionLoadVo lastYearSingle = new PollutionLoadVo();

        AtomicReference<Boolean> flag = new AtomicReference<>(false);
        pollutionLoadList.forEach(pollutionLoad -> {
            null2zero(pollutionLoad);
            if (StringUtils.equals(years[1], pollutionLoad.getYear())) {
                // 如果是当前年，统计总数
                this.calcTotal(thisYearTotal, pollutionLoad);
                this.calcSingle(thisYearSingle, pollutionLoad);
            } else {
                this.calcTotal(lastYearTotal, pollutionLoad);
                this.calcSingle(lastYearSingle, pollutionLoad);
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
            PollutionLoadVo deltaEnv = new PollutionLoadVo();
            deltaEnv.setAmmoniaNitrogen(thisYearSingle.getAmmoniaNitrogen().subtract(lastYearSingle.getAmmoniaNitrogen()));
            deltaEnv.setAnnualPrecipitation(thisYearSingle.getAnnualPrecipitation().subtract(lastYearSingle.getAnnualPrecipitation()));
            deltaEnv.setFulfurDioxide(thisYearSingle.getFulfurDioxide().subtract(lastYearSingle.getFulfurDioxide()));
            deltaEnv.setOxygenDemand(thisYearSingle.getOxygenDemand().subtract(lastYearSingle.getOxygenDemand()));
            deltaEnv.setOxynirtide(thisYearSingle.getOxynirtide().subtract(lastYearSingle.getOxynirtide()));
            deltaEnv.setSmokeDust(thisYearSingle.getSmokeDust().subtract(lastYearSingle.getSmokeDust()));
            deltaEnv.setSolidWaste(thisYearSingle.getSolidWaste().subtract(lastYearSingle.getSolidWaste()));
            delta.put("single", deltaEnv);
        } else {
            delta.put("total", new BigDecimal(0));
            delta.put("single", 0);
        }

        map.put("delta", delta);
        return map;
    }

    private MyLambdaQueryWrapper<PollutionLoad> createQueryWrapper(HttpServletRequest request) {
        MyLambdaQueryWrapper<PollutionLoad> queryWrapper = new MyLambdaQueryWrapper<>();
        String year = request.getParameter("year");
        if (StringUtils.isNotEmpty(year)) {
            queryWrapper.eq(PollutionLoad::getYear, year);
        }

        return queryWrapper;
    }

    private void calcTotal(AtomicReference<BigDecimal> total, PollutionLoad pollutionLoad) {
        total.set(total.get().add(pollutionLoad.getAmmoniaNitrogen() == null ? zero :
                pollutionLoad.getAmmoniaNitrogen())
                .add(pollutionLoad.getAnnualPrecipitation())
                .add(pollutionLoad.getFulfurDioxide())
                .add(pollutionLoad.getOxygenDemand())
                .add(pollutionLoad.getOxynirtide())
                .add(pollutionLoad.getSmokeDust())
                .add(pollutionLoad.getSolidWaste()));
    }

    private void calcSingle(PollutionLoadVo e1, PollutionLoad e2) {
        e1.setSolidWaste(e1.getSolidWaste().add(e2.getSolidWaste()));
        e1.setSmokeDust(e1.getSmokeDust().add(e2.getSmokeDust()));
        e1.setOxynirtide(e1.getOxynirtide().add(e2.getOxynirtide()));
        e1.setOxygenDemand(e1.getOxygenDemand().add(e2.getOxygenDemand()));
        e1.setFulfurDioxide(e1.getFulfurDioxide().add(e1.getFulfurDioxide()));
        e1.setAnnualPrecipitation(e1.getAnnualPrecipitation().add(e2.getAnnualPrecipitation()));
        e1.setAmmoniaNitrogen(e1.getAmmoniaNitrogen().add(e2.getAmmoniaNitrogen()));
    }

    private void null2zero(PollutionLoad pollutionLoad) {
        pollutionLoad.setSolidWaste(pollutionLoad.getSolidWaste() == null ? zero : pollutionLoad.getSolidWaste());
        pollutionLoad.setSmokeDust(pollutionLoad.getSmokeDust() == null ? zero : pollutionLoad.getSmokeDust());
        pollutionLoad.setOxynirtide(pollutionLoad.getOxynirtide() == null ? zero : pollutionLoad.getOxynirtide());
        pollutionLoad.setOxygenDemand(pollutionLoad.getOxygenDemand() == null ? zero : pollutionLoad.getOxygenDemand());
        pollutionLoad.setFulfurDioxide(pollutionLoad.getFulfurDioxide() == null ? zero : pollutionLoad.getFulfurDioxide());
        pollutionLoad.setAnnualPrecipitation(pollutionLoad.getAnnualPrecipitation() == null ? zero : pollutionLoad.getAnnualPrecipitation());
        pollutionLoad.setAmmoniaNitrogen(pollutionLoad.getAmmoniaNitrogen() == null ? zero : pollutionLoad.getAnnualPrecipitation());
    }
}

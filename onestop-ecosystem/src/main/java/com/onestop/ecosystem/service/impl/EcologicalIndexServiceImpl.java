package com.onestop.ecosystem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyLambdaQueryWrapper;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.common.service.impl.ServiceWithRedisImpl;
import com.onestop.common.util.BeanMapper;
import com.onestop.ecosystem.service.*;
import com.onestop.ecosystem.vo.EcologicalIndexVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.onestop.ecosystem.mapper.EcologicalIndexMapper;
import com.onestop.ecosystem.entity.EcologicalIndex;

@Service("ecologicalIndexService")
@Slf4j
public class EcologicalIndexServiceImpl extends ServiceWithRedisImpl<EcologicalIndexMapper, EcologicalIndex> implements EcologicalIndexService{
    @Resource
    private BiologicalAbundanceService biologicalAbundanceService;

    @Resource
    private EnviromentRestricationService enviromentRestricationService;

    @Resource
    private LandStressService landStressService;

    @Resource
    private PollutionLoadService pollutionLoadService;

    @Resource
    private RiversDensityService riversDensityService;

    @Resource
    private VegetationalCoverService vegetationalCoverService;

    @Override
    public MyPage<EcologicalIndexVo> getPage(Page page, HttpServletRequest request) {
        if (page.ascs() == null && page.descs() == null) {
            page.setAsc("code");
        }
        MyLambdaQueryWrapper<EcologicalIndex> queryWrapper = this.createQueryWrapper(request);
        MyPage<EcologicalIndex> ecologicalIndexMyPage = this.page(queryWrapper, page, true);
        MyPage<EcologicalIndexVo> ecologicalIndexMyPageVo = new MyPage<>();
        ecologicalIndexMyPageVo.setTotal(ecologicalIndexMyPage.getTotal());
        List<EcologicalIndex> ecologicalIndiceList = ecologicalIndexMyPage.getRecords();
        List<EcologicalIndexVo> ecologicalIndexVoList = BeanMapper.mapList(ecologicalIndiceList, EcologicalIndexVo.class);
        ecologicalIndexMyPageVo.setRecords(ecologicalIndexVoList);
        return ecologicalIndexMyPageVo;
    }

    @Override
    public MyPage getDetail(Page page, HttpServletRequest request) {
        String type = request.getParameter("type");
        MyPage myPage = new MyPage();
        switch (type) {
            case "1":
                myPage = biologicalAbundanceService.list(page, request);
                break;
            case "2":
                myPage = vegetationalCoverService.list(page, request);
                break;
            case "3":
                myPage = riversDensityService.list(page, request);
                break;
            case "4":
                myPage = landStressService.list(page, request);
                break;
            case "5":
                myPage = pollutionLoadService.list(page, request);
                break;
            case "6":
                myPage = enviromentRestricationService.list(page, request);
                break;
            default:
        }
        return myPage;
    }

    @Override
    public Map statistic(String countries, String year) {
        String[] countryArray = StringUtils.split(countries, ",");
        int lastYear = Integer.parseInt(year) - 1;
        String[] years = {String.valueOf(lastYear), year};
        Map<String, Map> map = new HashMap<>(8);

        Map<String, Map> eiMap = new HashMap<>(countryArray.length);
        Map<String, BigDecimal> deltaEiMap = new HashMap<>(countryArray.length);

        map.put("ei", eiMap);
        map.put("deltaEi", deltaEiMap);
        for (String country : countryArray) {
            Map<String, Map<String, BigDecimal>> eiMapEle = new HashMap<>(2);
            eiMap.put(country, eiMapEle);
            deltaEiMap.put(country, new BigDecimal(0));
        }


        MyLambdaQueryWrapper<EcologicalIndex> queryWrapper = new MyLambdaQueryWrapper<>();
        queryWrapper.in(EcologicalIndex::getCountry, countryArray);
        queryWrapper.in(EcologicalIndex::getYear, years);
        queryWrapper.orderByDesc(EcologicalIndex::getYear);
        List<EcologicalIndex> ecologicalIndexList = this.list(queryWrapper);

        ecologicalIndexList.forEach(ecologicalIndex -> {
            String country = ecologicalIndex.getCountry();
            String currentYear = ecologicalIndex.getYear();
            eiMap.get(country).put(currentYear, ecologicalIndex.getEcologicalRegime());
            eiMap.get(country).put("label", country);
        });

        eiMap.forEach((k, v) -> {
            if (v.size() == 3) {
                BigDecimal lastYearValue = (BigDecimal) v.get(years[0]);
                BigDecimal thisYearValue = (BigDecimal) v.get(years[1]);
                try {
                    deltaEiMap.put(k, thisYearValue.subtract(lastYearValue));
                } catch (NullPointerException e) {
                    log.error("k = {} 的数据有问题，{} 年的值为 {}，{} 年的值为 {}", k, years[0], lastYearValue, years[1], thisYearValue);
                }

            }
        });
        Map env = enviromentRestricationService.statistic(countryArray, years);
        map.put("env", env);
        Map pol = pollutionLoadService.statistic(countryArray, years);
        map.put("pol", pol);
        Map land = landStressService.statistic(countryArray, years);
        map.put("land", land);
        return map;
    }

    private MyLambdaQueryWrapper<EcologicalIndex> createQueryWrapper(HttpServletRequest request) {
        MyLambdaQueryWrapper<EcologicalIndex> queryWrapper = new MyLambdaQueryWrapper<>();
        String year = request.getParameter("year");
        if (StringUtils.isNotEmpty(year)) {
            queryWrapper.eq(EcologicalIndex::getYear, year);
        }
        return queryWrapper;
    }

}

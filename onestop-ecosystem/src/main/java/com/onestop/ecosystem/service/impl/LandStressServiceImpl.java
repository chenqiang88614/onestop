package com.onestop.ecosystem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyLambdaQueryWrapper;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.common.service.impl.ServiceWithRedisImpl;
import com.onestop.common.util.BeanMapper;
import com.onestop.ecosystem.entity.LandStress;
import com.onestop.ecosystem.mapper.LandStressMapper;
import com.onestop.ecosystem.service.LandStressService;
import com.onestop.ecosystem.vo.LandStressVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
@Service("landStressService")
public class LandStressServiceImpl extends ServiceWithRedisImpl<LandStressMapper, LandStress> implements LandStressService{
    private static BigDecimal zero = new BigDecimal(0);
    @Override
    public MyPage<LandStressVo> list(Page page, HttpServletRequest request) {
        if (page.ascs() == null && page.descs() == null) {
            page.setAsc("code");
        }
        MyLambdaQueryWrapper<LandStress> queryWrapper = this.createQueryWrapper(request);
        MyPage<LandStress> landStressMyPage = this.page(queryWrapper, page, true);
        MyPage<LandStressVo> landStressVoMyPage = new MyPage<>();
        landStressVoMyPage.setTotal(landStressMyPage.getTotal());
        List<LandStress> landStressList = landStressMyPage.getRecords();
        List<LandStressVo> landStressVoList = BeanMapper.mapList(landStressList,
                LandStressVo.class);
        landStressVoMyPage.setRecords(landStressVoList);
        return landStressVoMyPage;
    }

    @Override
    public Map statistic(String[] countryArray, String[] years) {
        Map<String, Map> map = new HashMap<>();
        MyLambdaQueryWrapper<LandStress> queryWrapper = new MyLambdaQueryWrapper<>();
        queryWrapper.in(LandStress::getCountry, countryArray);
        queryWrapper.in(LandStress::getYear, years);
        queryWrapper.orderByDesc(LandStress::getYear);
        List<LandStress> landStressList = this.list(queryWrapper);

        AtomicReference<BigDecimal> thisYearTotal = new AtomicReference<>(new BigDecimal(0));
        AtomicReference<BigDecimal> lastYearTotal = new AtomicReference<>(new BigDecimal(0));
        LandStressVo thisYearSingle = new LandStressVo();
        LandStressVo lastYearSingle = new LandStressVo();

        AtomicReference<Boolean> flag = new AtomicReference<>(false);

        landStressList.forEach(landStress -> {
            null2zero(landStress);
            if (StringUtils.equals(years[1], landStress.getYear())) {
                // 如果是当前年，统计总数
                thisYearTotal.set(thisYearTotal.get().add(landStress.getModerateErosion())
                        .add(landStress.getOtherLand())
                        .add(landStress.getSevereErosion())
                        .add(landStress.getTotalLand()));
                this.calcSingle(thisYearSingle, landStress);
            } else {
                lastYearTotal.set(lastYearTotal.get().add(landStress.getModerateErosion())
                        .add(landStress.getOtherLand())
                        .add(landStress.getSevereErosion())
                        .add(landStress.getTotalLand()));
                this.calcSingle(lastYearSingle, landStress);
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
            LandStressVo deltaEnv = new LandStressVo();
            deltaEnv.setModerateErosion(thisYearSingle.getModerateErosion().subtract(lastYearSingle.getModerateErosion()));
            deltaEnv.setOtherLand(thisYearSingle.getOtherLand().subtract(lastYearSingle.getOtherLand()));
            deltaEnv.setSevereErosion(thisYearSingle.getSevereErosion().subtract(lastYearSingle.getSevereErosion()));
            deltaEnv.setTotalLand(thisYearSingle.getTotalLand().subtract(lastYearSingle.getTotalLand()));
            delta.put("single", deltaEnv);
        } else {
            delta.put("total", new BigDecimal(0));
            delta.put("single", 0);
        }

        map.put("delta", delta);
        return map;
    }

    private MyLambdaQueryWrapper<LandStress> createQueryWrapper(HttpServletRequest request) {
        MyLambdaQueryWrapper<LandStress> queryWrapper = new MyLambdaQueryWrapper<>();
        String year = request.getParameter("year");
        if (StringUtils.isNotEmpty(year)) {
            queryWrapper.eq(LandStress::getYear, year);
        }

        return queryWrapper;
    }

    private void calcSingle(LandStressVo e1, LandStress e2) {
        e1.setTotalLand(e1.getTotalLand().add(e2.getTotalLand()));
        e1.setSevereErosion(e1.getSevereErosion().add(e2.getSevereErosion()));
        e1.setOtherLand(e1.getOtherLand().add(e2.getOtherLand()));
        e1.setModerateErosion(e1.getModerateErosion().add(e2.getModerateErosion()));
    }

    private void null2zero(LandStress landStress) {
        landStress.setModerateErosion(landStress.getModerateErosion() == null ? zero : landStress.getModerateErosion());
        landStress.setSevereErosion(landStress.getSevereErosion() == null ? zero : landStress.getSevereErosion());
        landStress.setOtherLand(landStress.getOtherLand() == null ? zero : landStress.getOtherLand());
        landStress.setTotalLand(landStress.getTotalLand() == null ? zero : landStress.getTotalLand());
    }
}

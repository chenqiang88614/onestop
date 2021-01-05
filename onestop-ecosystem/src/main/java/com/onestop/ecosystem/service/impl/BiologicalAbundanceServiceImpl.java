package com.onestop.ecosystem.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyLambdaQueryWrapper;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.common.service.impl.ServiceWithRedisImpl;
import com.onestop.common.util.BeanMapper;
import com.onestop.ecosystem.entity.BiologicalAbundance;
import com.onestop.ecosystem.entity.EcologicalIndex;
import com.onestop.ecosystem.vo.BiologicalAbundanceVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.onestop.ecosystem.mapper.BiologicalAbundanceMapper;
import com.onestop.ecosystem.service.BiologicalAbundanceService;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("biologicalAbundanceService")
public class BiologicalAbundanceServiceImpl extends ServiceWithRedisImpl<BiologicalAbundanceMapper, BiologicalAbundance> implements BiologicalAbundanceService {

    @Override
    public MyPage<BiologicalAbundanceVo> list(Page page, HttpServletRequest request) {
        if (page.ascs() == null && page.descs() == null) {
            page.setAsc("code");
        }
        MyLambdaQueryWrapper<BiologicalAbundance> queryWrapper = this.createQueryWrapper(request);
        MyPage<BiologicalAbundance> biologicalAbundanceIPage = this.page(queryWrapper, page, true);
        MyPage<BiologicalAbundanceVo> biologicalAbundanceVoMyPage = new MyPage<>();
        biologicalAbundanceVoMyPage.setTotal(biologicalAbundanceIPage.getTotal());
        List<BiologicalAbundance> biologicalAbundanceList = biologicalAbundanceIPage.getRecords();
        List<BiologicalAbundanceVo> biologicalAbundanceVoList = BeanMapper.mapList(biologicalAbundanceList,
                BiologicalAbundanceVo.class);
        biologicalAbundanceVoMyPage.setRecords(biologicalAbundanceVoList);
        return biologicalAbundanceVoMyPage;
    }

    private MyLambdaQueryWrapper<BiologicalAbundance> createQueryWrapper(HttpServletRequest request) {
        MyLambdaQueryWrapper<BiologicalAbundance> queryWrapper = new MyLambdaQueryWrapper<>();
        String year = request.getParameter("year");
        if (StringUtils.isNotEmpty(year)) {
            queryWrapper.eq(BiologicalAbundance::getYear, year);
        }

        return queryWrapper;
    }
}


package com.onestop.ecosystem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onestop.common.mybatisplus.expand.MyLambdaQueryWrapper;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.common.service.impl.ServiceWithRedisImpl;
import com.onestop.common.util.BeanMapper;
import com.onestop.ecosystem.entity.RiversDensity;
import com.onestop.ecosystem.entity.RiversDensity;
import com.onestop.ecosystem.mapper.RiversDensityMapper;
import com.onestop.ecosystem.service.RiversDensityService;
import com.onestop.ecosystem.vo.RiversDensityVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service("riversDensityService")
public class RiversDensityServiceImpl extends ServiceWithRedisImpl<RiversDensityMapper, RiversDensity> implements RiversDensityService{

    @Override
    public MyPage<RiversDensityVo> list(Page page, HttpServletRequest request) {
        if (page.ascs() == null && page.descs() == null) {
            page.setAsc("code");
        }
        MyLambdaQueryWrapper<RiversDensity> queryWrapper = this.createQueryWrapper(request);
        MyPage<RiversDensity> biologicalAbundanceIPage = this.page(queryWrapper, page, true);
        MyPage<RiversDensityVo> biologicalAbundanceVoMyPage = new MyPage<>();
        biologicalAbundanceVoMyPage.setTotal(biologicalAbundanceIPage.getTotal());
        List<RiversDensity> biologicalAbundanceList = biologicalAbundanceIPage.getRecords();
        List<RiversDensityVo> biologicalAbundanceVoList = BeanMapper.mapList(biologicalAbundanceList,
                RiversDensityVo.class);
        biologicalAbundanceVoMyPage.setRecords(biologicalAbundanceVoList);
        return biologicalAbundanceVoMyPage;
    }

    private MyLambdaQueryWrapper<RiversDensity> createQueryWrapper(HttpServletRequest request) {
        MyLambdaQueryWrapper<RiversDensity> queryWrapper = new MyLambdaQueryWrapper<>();
        String year = request.getParameter("year");
        if (StringUtils.isNotEmpty(year)) {
            queryWrapper.eq(RiversDensity::getYear, year);
        }

        return queryWrapper;
    }
}

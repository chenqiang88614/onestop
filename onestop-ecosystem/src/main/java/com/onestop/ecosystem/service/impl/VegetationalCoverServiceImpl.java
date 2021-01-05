package com.onestop.ecosystem.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyLambdaQueryWrapper;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.common.service.impl.ServiceWithRedisImpl;
import com.onestop.common.util.BeanMapper;
import com.onestop.ecosystem.entity.VegetationalCover;
import com.onestop.ecosystem.mapper.VegetationalCoverMapper;
import com.onestop.ecosystem.service.VegetationalCoverService;
import com.onestop.ecosystem.vo.VegetationalCoverVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Service("vegetationalCoverService")
public class VegetationalCoverServiceImpl extends ServiceWithRedisImpl<VegetationalCoverMapper, VegetationalCover> implements VegetationalCoverService{
    @Override
    public MyPage<VegetationalCoverVo> list(Page page, HttpServletRequest request) {
        if (page.ascs() == null && page.descs() == null) {
            page.setAsc("code");
        }
        MyLambdaQueryWrapper<VegetationalCover> queryWrapper = this.createQueryWrapper(request);
        MyPage<VegetationalCover> biologicalAbundanceIPage = this.page(queryWrapper, page, true);
        MyPage<VegetationalCoverVo> biologicalAbundanceVoMyPage = new MyPage<>();
        biologicalAbundanceVoMyPage.setTotal(biologicalAbundanceIPage.getTotal());
        List<VegetationalCover> biologicalAbundanceList = biologicalAbundanceIPage.getRecords();
        List<VegetationalCoverVo> biologicalAbundanceVoList = BeanMapper.mapList(biologicalAbundanceList,
                VegetationalCoverVo.class);
        biologicalAbundanceVoMyPage.setRecords(biologicalAbundanceVoList);
        return biologicalAbundanceVoMyPage;
    }

    @Override
    public Map statistic(String[] countryArray, String[] years) {
        return null;
    }

    private MyLambdaQueryWrapper<VegetationalCover> createQueryWrapper(HttpServletRequest request) {
        MyLambdaQueryWrapper<VegetationalCover> queryWrapper = new MyLambdaQueryWrapper<>();
        String year = request.getParameter("year");
        if (StringUtils.isNotEmpty(year)) {
            queryWrapper.eq(VegetationalCover::getYear, year);
        }

        return queryWrapper;
    }
}

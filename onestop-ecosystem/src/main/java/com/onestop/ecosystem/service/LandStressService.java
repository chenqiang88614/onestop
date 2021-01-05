package com.onestop.ecosystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.ecosystem.entity.LandStress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.onestop.ecosystem.vo.BiologicalAbundanceVo;
import com.onestop.ecosystem.vo.LandStressVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface LandStressService extends IService<LandStress>{

    MyPage<LandStressVo> list(Page page, HttpServletRequest request);

    Map statistic(String[] countryArray, String[] years);
}

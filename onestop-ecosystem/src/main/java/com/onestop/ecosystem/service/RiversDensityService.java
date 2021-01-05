package com.onestop.ecosystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.ecosystem.entity.RiversDensity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.onestop.ecosystem.vo.BiologicalAbundanceVo;
import com.onestop.ecosystem.vo.RiversDensityVo;

import javax.servlet.http.HttpServletRequest;

public interface RiversDensityService extends IService<RiversDensity>{

    MyPage<RiversDensityVo> list(Page page, HttpServletRequest request);
}

package com.onestop.ecosystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.ecosystem.entity.BiologicalAbundance;
import com.baomidou.mybatisplus.extension.service.IService;
import com.onestop.ecosystem.vo.BiologicalAbundanceVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface BiologicalAbundanceService extends IService<BiologicalAbundance> {

    MyPage<BiologicalAbundanceVo> list(Page page, HttpServletRequest request);


}


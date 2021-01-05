package com.onestop.ecosystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.ecosystem.entity.EcologicalIndex;
import com.baomidou.mybatisplus.extension.service.IService;
import com.onestop.ecosystem.vo.EcologicalIndexVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface EcologicalIndexService extends IService<EcologicalIndex>{

    MyPage<EcologicalIndexVo> getPage(Page page, HttpServletRequest request);

    MyPage getDetail(Page page, HttpServletRequest request);

    Map statistic(String countrys, String year);
}

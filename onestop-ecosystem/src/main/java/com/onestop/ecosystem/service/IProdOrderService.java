package com.onestop.ecosystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.ecosystem.entity.ProdOrder;
import com.onestop.ecosystem.vo.ConfirmVo;
import com.onestop.ecosystem.vo.ProdOrderVo;

import javax.servlet.http.HttpServletRequest;

/**
 * @description 产品定制单业务层接口
 * @author chenq
 * @date 2019年9月4日09:11:40
 */
public interface IProdOrderService extends IService<ProdOrder>{

    MyPage<ProdOrder> getPage(Page page, HttpServletRequest request);

    MyPage<ProdOrderVo> getPageWithVo(Page page, HttpServletRequest request);

    String confirm(ConfirmVo confirm);

    Boolean checkExistByOrderId(String orderId);


}

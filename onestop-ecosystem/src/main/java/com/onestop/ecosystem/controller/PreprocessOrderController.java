package com.onestop.ecosystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.common.util.PageFactory;
import com.onestop.ecosystem.service.IPreprocessOrderService;
import com.onestop.ecosystem.util.ResultUtil;
import com.onestop.ecosystem.vo.ConfirmVo;
import com.onestop.ecosystem.vo.PreprocessOrderVo;
import com.onestop.ecosystem.vo.ProdOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;

/**
 * @description: 预处理订单controller
 * @author: chenq
 * @date: 2019/9/6 15:58
 */
@RestController
@RequestMapping("preprocess")
@Api
@CrossOrigin(origins = "*", allowCredentials = "true", maxAge = 3600)
public class PreprocessOrderController {

    @Resource
    private IPreprocessOrderService preprocessOrderService;

    @GetMapping("list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPageNum", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页数量", paramType = "query"),
            @ApiImplicitParam(name = "sortIndex", value = "索引字段，与数据库表结构一致，采用下划线连接", paramType = "query"),
            @ApiImplicitParam(name = "sortUp", value = "排序:true表示降序，false表示升序 ", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "专题产品定制单ProdOrder的uuid ", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "专题产品定制单ID ", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态 ", paramType = "query"),
    })

    public @ResponseBody ModelMap list(HttpServletRequest request) {
        Page page = PageFactory.INSTANCE.defaultPage();
        MyPage<PreprocessOrderVo> preprocessOrderVoMyPage = preprocessOrderService.list(page, request);
        ModelMap modelMap = ResultUtil.success(preprocessOrderVoMyPage);
        return modelMap;
    }

    @GetMapping("confirm")
    @ApiImplicitParam(name = "id", value = "id ", paramType = "query")
    public @ResponseBody ModelMap confirm(@WebParam String id) {
        String result = preprocessOrderService.doProcess(id);
        ModelMap modelMap;
        if (result == null) {
            modelMap = ResultUtil.success();
        } else {
            modelMap = ResultUtil.error(result);
        }
        return modelMap;
    }
}

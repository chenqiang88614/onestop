package com.onestop.ecosystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.common.util.PageFactory;
import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.entity.EcologicalIndex;
import com.onestop.ecosystem.service.EcologicalIndexService;
import com.onestop.ecosystem.service.IExcelService;
import com.onestop.ecosystem.util.ResultUtil;
import com.onestop.ecosystem.vo.EcologicalIndexVo;
import com.onestop.ecosystem.vo.ProdOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @description: EI控制类
 * @author: chenq
 * @date: 2019/9/26 11:01
 */
@RestController
@RequestMapping("ecoindex")
@Slf4j
@Api
@CrossOrigin(origins = "*", allowCredentials = "true", maxAge = 3600)
public class EcologicalIndexController {
    @Resource
    private IExcelService excelService;

    @Resource
    private EcologicalIndexService ecologicalIndexService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping("create")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "文件路径 ", paramType = "query"),
            @ApiImplicitParam(name = "year", value = "年份 ", paramType = "query")
    })
    public @ResponseBody ModelMap create(@WebParam String year, @WebParam String path) {

        ModelMap modelMap = new ModelMap();
        String result = excelService.add(year, path);
        return ResultUtil.success();
    }

    @GetMapping("list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPageNum", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页数量", paramType = "query"),
            @ApiImplicitParam(name = "sortIndex", value = "索引字段，与数据库表结构一致，采用下划线连接", paramType = "query"),
            @ApiImplicitParam(name = "sortUp", value = "排序:true表示降序，false表示升序 ", paramType = "query"),
            @ApiImplicitParam(name = "year", value = "年份 ", paramType = "query")
          })
    public @ResponseBody
    ModelMap list(HttpServletRequest request) {
        Page page = PageFactory.INSTANCE.defaultPage();
        MyPage<EcologicalIndexVo> ecologicalIndexMyPage = ecologicalIndexService.getPage(page, request);
        ModelMap modelMap = ResultUtil.success(ecologicalIndexMyPage);
        return modelMap;
    }

    @GetMapping("detail")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPageNum", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页数量", paramType = "query"),
            @ApiImplicitParam(name = "sortIndex", value = "索引字段，与数据库表结构一致，采用下划线连接", paramType = "query"),
            @ApiImplicitParam(name = "sortUp", value = "排序:true表示降序，false表示升序 ", paramType = "query"),
            @ApiImplicitParam(name = "year", value = "年份 ", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "类型 ", paramType = "query")
    })
    public @ResponseBody
    ModelMap detail(HttpServletRequest request) {
        Page page = PageFactory.INSTANCE.defaultPage();
        MyPage myPage = ecologicalIndexService.getDetail(page, request);
        ModelMap modelMap = ResultUtil.success(myPage);
        return modelMap;
    }

    @GetMapping("statistic")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "年份 ", paramType = "query"),
            @ApiImplicitParam(name = "countries", value = "县区，多个县区以‘,’分割 ", paramType = "query")
    })
    public @ResponseBody ModelMap statistic(@WebParam String countries, @WebParam String year) {
        Map map = ecologicalIndexService.statistic(countries, year);
        return ResultUtil.success(map);
    }

    @GetMapping("countrycode")
    public @ResponseBody ModelMap countryCode() {
        return ResultUtil.success(redisTemplate.opsForHash().entries(RedisKey.COUNTRY_CODE_TO_REDIS_PREX.getKey() + "2"));
    }
}

package com.onestop.ecosystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.common.util.PageFactory;
import com.onestop.ecosystem.service.IOrderStatusService;
import com.onestop.ecosystem.util.ResultUtil;
import com.onestop.ecosystem.vo.PreprocessOrderVo;
import com.onestop.ftpclient.service.FtpParamService;
import com.onestop.ftpclient.spi.IFtpOperation;
import com.onestop.ftpclient.util.FtpParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @description: 配置FTP
 * @author: chenq
 * @date: 2019/9/6 8:30
 */
@RestController
@RequestMapping("ftp")
@Api
@CrossOrigin(origins = "*", allowCredentials = "true", maxAge = 3600)
public class FtpConfigController {
    @Resource
    private FtpParamService ftpParamService;

    @GetMapping("list")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPageNum", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页数量", paramType = "query"),
            @ApiImplicitParam(name = "sortIndex", value = "索引字段，与数据库表结构一致，采用下划线连接", paramType = "query"),
            @ApiImplicitParam(name = "sortUp", value = "排序:true表示降序，false表示升序 ", paramType = "query")
    })
    public @ResponseBody ModelMap list(HttpServletRequest request) {
        Page page = PageFactory.INSTANCE.defaultPage();
        MyPage<FtpParam> ftpParamMyPage = ftpParamService.getFtpParamPage(page, request);
        ModelMap modelMap = ResultUtil.success(ftpParamMyPage);
        return modelMap;
    }

    @PostMapping("create")
    public @ResponseBody ModelMap create(@RequestBody FtpParam ftpParam) {
        ftpParamService.create(ftpParam);
        return ResultUtil.success();
    }

    @PostMapping("update")
    public @ResponseBody ModelMap update(@RequestBody FtpParam ftpParam) {
        ftpParamService.modify(ftpParam);
        return ResultUtil.success();
    }
}

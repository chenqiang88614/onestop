package com.onestop.ecosystem.controller;

import com.onestop.ecosystem.service.IOrderStatusService;
import com.onestop.ecosystem.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.jws.WebParam;
import java.util.Map;

/**
 * @description: 对外提供订单状态服务
 * @author: chenq
 * @date: 2019/9/6 8:30
 */
@RestController
@RequestMapping("order-status")
@Api
@CrossOrigin(origins = "*", allowCredentials = "true", maxAge = 3600)
public class OrderStatusController {
    @Resource
    private IOrderStatusService orderStatusService;

    @GetMapping("list")
    @ApiImplicitParam(name = "name", value = "专题产品定制单：PROD_ORDER_STATUS；预处理订单：PRE_PROCESS_ORDER_STATUS ", paramType = "query")
    public @ResponseBody
    ModelMap list(@WebParam String name) {
        Map map = orderStatusService.list(name);
        return ResultUtil.success(map);
    }
}

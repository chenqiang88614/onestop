package com.onestop.ecosystem.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.common.util.PageFactory;
import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.service.IProdOrderService;
import com.onestop.ecosystem.util.ResultUtil;
import com.onestop.ecosystem.vo.ConfirmVo;
import com.onestop.ecosystem.vo.ProdOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api
@RestController
@RequestMapping("prod-order")
@CrossOrigin(origins = "*", allowCredentials = "true", maxAge = 3600)
public class ProdOrderController {
    @Resource
    private IProdOrderService prodOrderService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPageNum", value = "当前页", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页数量", paramType = "query"),
            @ApiImplicitParam(name = "sortIndex", value = "索引字段，与数据库表结构一致，采用下划线连接", paramType = "query"),
            @ApiImplicitParam(name = "sortUp", value = "排序:true表示降序，false表示升序 ", paramType = "query"),
            @ApiImplicitParam(name = "startTime", value = "开始时间 ", paramType = "query"),
            @ApiImplicitParam(name = "endTime", value = "结束时间 ", paramType = "query"),
            @ApiImplicitParam(name = "satelliteId", value = "卫星 ", paramType = "query"),
            @ApiImplicitParam(name = "orderId", value = "任务单Id ", paramType = "query"),
            @ApiImplicitParam(name = "thematicType", value = "专题产品类型 ", paramType = "query"),
            @ApiImplicitParam(name = "urgencyLevel", value = "紧急程度 ", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "任务状态 ", paramType = "query")})
    public @ResponseBody
    ModelMap list(HttpServletRequest request) {
        Page page = PageFactory.INSTANCE.defaultPage();
        MyPage<ProdOrderVo> prodOrderMyPage = prodOrderService.getPageWithVo(page, request);
        ModelMap modelMap = ResultUtil.success(prodOrderMyPage);
        return modelMap;
    }

    @PutMapping
    public @ResponseBody ModelMap confirm(@RequestBody ConfirmVo confirm) {
        ModelMap modelMap;
        String reason = checkParam(confirm);
        if (reason != null) {
            modelMap = ResultUtil.error("1", reason, confirm);
        } else {
            reason = prodOrderService.confirm(confirm);
            if (reason != null) {
                modelMap = ResultUtil.error("1", reason);
            } else {
                modelMap = ResultUtil.success();
            }
        }
        return modelMap;
    }

    @GetMapping("thematicType")
    public @ResponseBody ModelMap getThematicType() {
        Map map = redisTemplate.opsForHash().entries(RedisKey.THEMATIC_TYPE.getKey());
        ModelMap modelMap = ResultUtil.success(map);
        return modelMap;
    }

    private String checkParam(ConfirmVo confirm) {
        String reason = null;
        if (StringUtils.isEmpty(confirm.getId())) {
            reason = "ID为空";
        } else if (!confirm.isConfirm() && StringUtils.isEmpty(confirm.getReason())) {
            reason = "未填写拒绝原因";
        }
        return reason;
    }
}

package com.onestop.ecosystem.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.ecosystem.entity.PreprocessOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.onestop.ecosystem.vo.ConfirmVo;
import com.onestop.ecosystem.vo.PreprocessOrderVo;
import org.springframework.data.redis.connection.Message;

import javax.servlet.http.HttpServletRequest;

public interface IPreprocessOrderService extends IService<PreprocessOrder>{

    MyPage<PreprocessOrderVo> list(Page page, HttpServletRequest request);

    PreprocessOrder getByOrderIdAndProductId(String orderId, String productId);

    PreprocessOrder getByTaskId(String taskId);

    void modifyTaskStatus(String orderId, String taskId, Integer status, String reason);

    void completeTask(PreprocessOrder preprocessOrder);

    void consumMessage(Message message, byte[] pattern);

    String doProcess(String id);
}

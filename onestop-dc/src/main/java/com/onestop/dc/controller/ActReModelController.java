package com.onestop.dc.controller;


import com.onestop.dao.bean.ActReModel;
import com.onestop.dc.service.ActReModelService;
import com.onestop.dc.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("model")
public class ActReModelController extends BaseController{

    @Autowired
    private ActReModelService actReModelService;

    /**
     * 获取所有模块名称
     * @return
     */
    @RequestMapping(value="/getAllModel",method = RequestMethod.POST)
    public Result getAllModel(){
        List<ActReModel> list = actReModelService.getAllModel();
        return success(list);
    }
}

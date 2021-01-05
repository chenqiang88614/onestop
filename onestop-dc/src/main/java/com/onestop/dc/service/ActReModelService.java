package com.onestop.dc.service;

import com.onestop.dao.bean.ActReModel;
import com.onestop.dao.mapper.ActReModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActReModelService {
    @Autowired
    private ActReModelMapper actReModelMapper;

    /**
     * 获取所有模块名称
     * @return
     */
    public List<ActReModel> getAllModel(){
        return actReModelMapper.getAllJobInfo();
    }
}

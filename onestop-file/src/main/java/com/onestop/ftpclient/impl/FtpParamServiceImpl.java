package com.onestop.ftpclient.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyLambdaQueryWrapper;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.common.service.impl.ServiceWithRedisImpl;
import com.onestop.common.util.SnowflakeIdWorker;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onestop.ftpclient.mapper.FtpParamMapper;
import com.onestop.ftpclient.util.FtpParam;
import com.onestop.ftpclient.service.FtpParamService;

@Service("ftpParamService")
public class FtpParamServiceImpl extends ServiceWithRedisImpl<FtpParamMapper, FtpParam> implements FtpParamService {
    @Resource
    private SnowflakeIdWorker snowflakeIdWorker;

    @Override
    public FtpParam getByServerName(String serverName) {
        LambdaQueryWrapper<FtpParam> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FtpParam::getServerName, serverName);
        return this.getOne(queryWrapper);
    }

    @Override
    public MyPage<FtpParam> getFtpParamPage(Page page, HttpServletRequest request) {
        MyLambdaQueryWrapper<FtpParam> queryWrapper = new MyLambdaQueryWrapper<>();
        MyPage<FtpParam> ftpParamMyPage = this.page(queryWrapper, page, true);
        return ftpParamMyPage;
    }

    @Override
    public String create(FtpParam ftpParam) {
        ftpParam.setId(snowflakeIdWorker.nextId());
        this.save(ftpParam);
        return null;
    }

    @Override
    public String modify(FtpParam ftpParam) {
        this.saveOrUpdate(ftpParam);
        return null;
    }
}


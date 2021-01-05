package com.onestop.ftpclient.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.onestop.common.mybatisplus.expand.MyPage;
import com.onestop.ftpclient.util.FtpParam;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

public interface FtpParamService extends IService<FtpParam> {

    FtpParam getByServerName(String serverName);

    MyPage<FtpParam> getFtpParamPage(Page page, HttpServletRequest request);

    String create(FtpParam ftpParam);

    String modify(FtpParam ftpParam);
}


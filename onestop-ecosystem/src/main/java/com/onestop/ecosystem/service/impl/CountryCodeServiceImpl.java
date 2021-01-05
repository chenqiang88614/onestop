package com.onestop.ecosystem.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onestop.ecosystem.mapper.CountryCodeMapper;
import com.onestop.ecosystem.entity.CountryCode;
import com.onestop.ecosystem.service.CountryCodeService;
@Service
public class CountryCodeServiceImpl extends ServiceImpl<CountryCodeMapper, CountryCode> implements CountryCodeService{

}

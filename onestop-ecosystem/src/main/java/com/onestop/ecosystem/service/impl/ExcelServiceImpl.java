package com.onestop.ecosystem.service.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.onestop.ecosystem.constant.Grade;
import com.onestop.ecosystem.constant.RedisKey;
import com.onestop.ecosystem.entity.*;
import com.onestop.ecosystem.excel.*;
import com.onestop.ecosystem.service.EcologicalIndexService;
import com.onestop.ecosystem.service.IExcelService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @description: excel处理类
 * @author: chenq
 * @date: 2019/9/26 11:11
 */
@Service("excelService")
public class ExcelServiceImpl implements IExcelService {
    @Value("${Configuration.customer.fsf.ftpfile-backup}")
    private String fileBackup;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private EcologicalIndexService ecologicalIndexService;

    @Override
    public String add(String year, String path) {
        File orignal = new File(path);
        if (!orignal.exists()) {
            return path + " 的文件不存在！";
        }
        String newPath = fileBackup + "/tmp/" + orignal.getName();
        FileUtil.copy(orignal, new File(newPath), true);
        try {
//            String newPath = path;
            String[] sheets = {"生境质量指数", "生物丰度指数", "植被覆盖指数", "水网密度指数", "土地胁迫指数", "污染负荷指数"};
            ExcelReader excelReader = EasyExcel.read(newPath).build();
            // 这里为了简单 所以注册了 同样的head 和Listener 自己使用功能必须不同的Listener
            ReadSheet readSheet1 =
                    EasyExcel.readSheet(sheets[0]).head(EnviromentRestrication.class).registerReadListener(new EnviromentRestricationListener(year)).build();
            ReadSheet readSheet2 =
                    EasyExcel.readSheet(sheets[1]).head(BiologicalAbundance.class).registerReadListener(new BiologicalAbundanceListener(year)).build();
            ReadSheet readSheet3 =
                    EasyExcel.readSheet(sheets[2]).head(VegetationalCover.class).registerReadListener(new VegetationalCoverListener(year)).build();
            ReadSheet readSheet4 =
                    EasyExcel.readSheet(sheets[3]).head(RiversDensity.class).registerReadListener(new RiversDensityListener(year)).build();
            ReadSheet readSheet5 =
                    EasyExcel.readSheet(sheets[4]).head(LandStress.class).registerReadListener(new LandStressListener(year)).build();
            ReadSheet readSheet6 =
                    EasyExcel.readSheet(sheets[5]).head(PollutionLoad.class).registerReadListener(new PollutionLaodListener(year)).build();
            excelReader.read(readSheet1);
            excelReader.read(readSheet2);
            excelReader.read(readSheet3);
            excelReader.read(readSheet4);
            excelReader.read(readSheet5);
            excelReader.read(readSheet6);
            // 这里千万别忘记关闭，读的时候会创建临时文件，到时磁盘会崩的
            excelReader.finish();

            // 将六个指数入库到EI_ECOLOGICAL_INDEX表中
            Map map = redisTemplate.opsForHash().entries(RedisKey.ECOLOGICAL_INDEX.getKey());
            BigDecimal percent = new BigDecimal(100);
            if (map.size() > 0) {
                map.forEach((k, v) -> {
                    try {
                        EcologicalIndex ecologicalIndex = (EcologicalIndex) v;
                        BigDecimal sum = ecologicalIndex.getBiologicalAbundance().multiply(new BigDecimal(0.35));
                        sum = sum.add(ecologicalIndex.getVegetationalCover().multiply(new BigDecimal(0.25)));
                        sum = sum.add(ecologicalIndex.getRiversDensity().multiply(new BigDecimal(0.15)));
                        BigDecimal diff = percent.subtract(ecologicalIndex.getLandStress());
                        sum = sum.add(diff.multiply(new BigDecimal(0.15)));
                        BigDecimal diff2 = percent.subtract(ecologicalIndex.getPollutionLoad());
                        sum = sum.add(diff2.multiply(new BigDecimal(0.1)));
                        ecologicalIndex.setEcologicalRegime(sum);

                        if (sum.compareTo(new BigDecimal(Grade.PERFECT.getGrade())) > -1) {
                            ecologicalIndex.setGrade(Grade.PERFECT.getName());
                        } else if (sum.compareTo(new BigDecimal(Grade.BEST.getGrade())) > -1) {
                            ecologicalIndex.setGrade(Grade.BEST.getName());
                        } else if (sum.compareTo(new BigDecimal(Grade.NORMAL.getGrade())) > -1) {
                            ecologicalIndex.setGrade(Grade.NORMAL.getName());
                        } else if (sum.compareTo(new BigDecimal(Grade.WORSE.getGrade())) > -1) {
                            ecologicalIndex.setGrade(Grade.WORSE.getName());
                        } else {
                            ecologicalIndex.setGrade(Grade.BAD.getName());
                        }
                    } catch (Exception e1) {
                        System.out.println(v.toString());
                    }

                });
                ecologicalIndexService.saveOrUpdateBatch(map.values());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            redisTemplate.delete(RedisKey.ECOLOGICAL_INDEX.getKey());
            Set keys = redisTemplate.keys("CACHE*");
            if (keys != null && keys.size() > 0) {
                redisTemplate.delete(keys);
            }

        }

        return null;
    }

    @Override
    public void statistic(String countrys, String year) {
        String[] countryArray = StringUtils.split(countrys, "#");

    }
}

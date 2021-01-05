package com.onestop.dc.service;

import com.onestop.dao.bean.Satellitedata;
import com.onestop.dao.mapper.SatellitedataMapper;
import com.onestop.dc.utils.DateUtils;
import com.onestop.dc.utils.PageInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class SatellitedataService {
    @Resource
    private SatellitedataMapper satellitedataMapper;

    /**
     * 分页条件查询
     * @param satelliteid 卫星id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageNumber 起始页码
     * @param pageSize 分页大小
     * @return satellite分页数据
     */
    public PageInfo<Satellitedata> querySatelliteListPaging(String satelliteid,
                                                  String startTime, String endTime,
                                                  Integer pageNumber, Integer pageSize){
        PageInfo<Satellitedata> page = new PageInfo<>(pageNumber,pageSize);
        List<Satellitedata> list = satellitedataMapper.queryJobListPaging(satelliteid, startTime, endTime, page.getStart(), page.getPageSize());
        Integer count = satellitedataMapper.countSatellites(satelliteid, startTime, endTime);
        page.setTotalCount(count);
        page.setLists(list);
        return page;
    }

    /**
     * 根据id查询
     * @param id 数据id
     * @return satellitedata
     */
    public Satellitedata queryById(String id){
        return satellitedataMapper.querySatelliteById(id);
    }


    /**
     * 获取压缩包同名文件夹下后缀为type的文件
     * @param dir
     * @param type
     * @return
     */
    public List<String> getFileByDirType(String dir, String type){
        String compressType1 = ".zip";
        String compressType2 = ".tar.gz";
        List<String> result = null;
        if(StringUtils.isNotEmpty(dir)) {
            String path = null;
            if(dir.endsWith(compressType1)){
                path = dir.substring(0, dir.length() - compressType1.length());
            }else if(dir.endsWith(compressType2)){
                path = dir.substring(0, dir.length() - compressType2.length());
            }
            result = iterPath(path, type);
        }
        return result;
    }

    /**
     * 遍历文件夹，获取文件后缀type的文件
     * @param path 文件夹
     * @param type 文件后缀
     * @return 结果
     */
    public List<String> iterPath(String path, String type){
        List<String> list = new ArrayList<String>();

        if(path != null){
            File file = new File(path);
            File[] result = file.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    String tl = type.substring(type.length()-1);
                    String tmpName = name.toLowerCase(); //解决后缀为.tif .Tif .TIF情况
                    if(tmpName.endsWith(type) || tmpName.endsWith(type.concat(tl))){
                        return true;
                    }else{
                        return false;
                    }
                }
            });

            for(File r : result){
                list.add(r.getAbsolutePath());
            }
        }
        return list;

    }

    public static void main(String[] args) {
        new SatellitedataService().getFileByDirType("Z:\\root\\data_storage_area\\GF1\\PMS2\\20140831\\2c97f95b6d19db1a016d19dc86930000\\GF1_PMS2_E123.8_N42.4_20140831_L1A0000431214.zip",
                ".tif");
    }
}

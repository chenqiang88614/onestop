package com.onestop.dc.controller;

import com.onestop.dao.bean.Satellitedata;
import com.onestop.dc.service.SatellitedataService;
import com.onestop.dc.utils.PageInfo;
import com.onestop.dc.utils.Result;

import com.onestop.dc.view.SatellitePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("satellite")
public class SatellitedataController extends BaseController{
    @Autowired
    private SatellitedataService satellitedataService;

    @RequestMapping(value="/getSatelliteByID",method = RequestMethod.POST)
    public Result getSatelliteByID(@RequestParam String id) {
        Satellitedata satellite = satellitedataService.queryById(id);
        return success(satellite);
    }
    @RequestMapping(value="/querySatelliteListPaging",method = RequestMethod.POST)
    public Result querySatelliteListPaging(@RequestBody SatellitePage satellite){
        PageInfo<Satellitedata> satellitePage = satellitedataService.querySatelliteListPaging(
                satellite.getSatelliteid(),
                satellite.getStartTime(),satellite.getEndTime(),
                satellite.getPageNumber(), satellite.getPageSize());
        return success(satellitePage);
    }
    @RequestMapping(value="/getPathsByDirType",method = RequestMethod.GET)
    public Result getPathsByDirType(String dir, String type){
        List<String> list = satellitedataService.getFileByDirType(dir, type);
        return success(list);
    }
}

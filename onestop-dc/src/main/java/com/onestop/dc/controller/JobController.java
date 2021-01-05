package com.onestop.dc.controller;

import com.onestop.dao.bean.Job;
import com.onestop.dc.service.JobService;
import com.onestop.dc.view.JobPage;
import com.onestop.dc.view.Page;
import com.onestop.dc.utils.PageInfo;
import com.onestop.dc.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowCredentials = "true", maxAge = 3600)
@RestController
@RequestMapping("job")
public class JobController extends BaseController{

    @Autowired
    private JobService jobService;

    @RequestMapping(value="/getJobInfoByID",method = RequestMethod.POST)
    public Result getJobInfoByID(@RequestParam Integer id) {
        Job job = jobService.getJobInfoByID(id);
        return success(job);
    }
    @RequestMapping(value="/queryJobListPaging",method = RequestMethod.POST)
    public Result queryJobListPaging(@RequestBody JobPage job){
        PageInfo<Job> jobPage = jobService.queryJobListPaging(
                job.getFlow_name(), job.getStatus(),
                job.getStartTime(),job.getEndTime(),
                job.getPageNumber(), job.getPageSize());
        return success(jobPage);
    }
    @RequestMapping(value="/getAllJobInfo",method = RequestMethod.POST)
    public Result getAllJobInfo(){
        List<Job> list = jobService.getAllJobInfo();
        return success(list);
    }
    @RequestMapping(value="/getJobInfoByFlowName",method = RequestMethod.POST)
    public Result getJobInfoByFlowName(@RequestParam  String flowName){
        List<Job> list = jobService.getJobInfoByFlowName(flowName);
        return success(list);
    }


}

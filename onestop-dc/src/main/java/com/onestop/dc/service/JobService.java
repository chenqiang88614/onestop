package com.onestop.dc.service;

import com.onestop.dao.bean.Job;
import com.onestop.dao.mapper.JobMapper;
import com.onestop.dc.utils.DateUtils;
import com.onestop.dc.utils.PageInfo;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class JobService {
    @Autowired
    private JobMapper jobMapper;

    public Job getJobInfoByID(Integer id){
        return jobMapper.getJobInfoById(id);
    }

    /**
     *
     * @return
     */
    public List<Job> getAllJobInfo(){
        return jobMapper.getAllJobInfo();
    }

    public PageInfo<Job> queryJobListPaging(String flowName, Integer status,
                                            String startTime, String endTime,
                                            Integer pageNumber, Integer pageSize){
        PageInfo<Job> page = new PageInfo<>(pageNumber,pageSize);
        Date start = null;
        Date end = null;
        if(StringUtils.isNotEmpty(startTime)){
            start = DateUtils.stringToDate(startTime);
        }
        if(StringUtils.isNotEmpty(endTime)){
            end = DateUtils.stringToDate(endTime);
        }
        List<Job> list = jobMapper.queryJobListPaging(flowName, status, start, end, page.getStart(), page.getPageSize());
        Integer count = jobMapper.countJobs(flowName, status, start, end);
        page.setTotalCount(count);
        page.setLists(list);
        return page;
    }

    /**
     *
     * @param flowName
     * @return
     */
    public List<Job> getJobInfoByFlowName(String flowName){
        return jobMapper.getJobInfoByFlowName(flowName);
    }

    /**
     *
     * @param jobName
     * @return
     */
    public List<Job> getJobInfoByJobName(String jobName){
        return jobMapper.getJobInfoByJobName(jobName);
    }


}

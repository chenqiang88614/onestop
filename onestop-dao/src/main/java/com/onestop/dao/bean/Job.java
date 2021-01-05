package com.onestop.dao.bean;

import java.util.Date;

public class Job {
    private int id;

    private String outputfile;
    private String current_task;
    private String description;
    private String fail_reason;
    private String flow_name;
    private String job_name;
    private String parma_info;
    private String reader_ids;

    private Date start_time;
    private Date create_time;
    private Date end_time;
    private Date plan_start_time;

    private int create_id;
    private int is_plan;
    private int job_type;
    private int priority;
    private int status;
    private int result;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getOutputfile() {
        return outputfile;
    }

    public void setOutputfile(String outputfile) {
        this.outputfile = outputfile;
    }

    public String getCurrent_task() {
        return current_task;
    }

    public void setCurrent_task(String current_task) {
        this.current_task = current_task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFail_reason() {
        return fail_reason;
    }

    public void setFail_reason(String fail_reason) {
        this.fail_reason = fail_reason;
    }

    public String getFlow_name() {
        return flow_name;
    }

    public void setFlow_name(String flow_name) {
        this.flow_name = flow_name;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getParma_info() {
        return parma_info;
    }

    public void setParma_info(String parma_info) {
        this.parma_info = parma_info;
    }

    public String getReader_ids() {
        return reader_ids;
    }

    public void setReader_ids(String reader_ids) {
        this.reader_ids = reader_ids;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public Date getPlan_start_time() {
        return plan_start_time;
    }

    public void setPlan_start_time(Date plan_start_time) {
        this.plan_start_time = plan_start_time;
    }

    public int getCreate_id() {
        return create_id;
    }

    public void setCreate_id(int create_id) {
        this.create_id = create_id;
    }

    public int getIs_plan() {
        return is_plan;
    }

    public void setIs_plan(int is_plan) {
        this.is_plan = is_plan;
    }

    public int getJob_type() {
        return job_type;
    }

    public void setJob_type(int job_type) {
        this.job_type = job_type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

package com.casic.jd.dao;

import com.casic.jd.bean.JobInfo;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface JobInfoDao {
    public void save(JobInfo jobInfo);
    public List<JobInfo> findAll(JobInfo jobInfo);
}

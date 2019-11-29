package com.casic.jd.service;

import com.casic.jd.bean.JobInfo;
import com.casic.jd.dao.JobInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class JobInfoService {
    @Autowired
    private JobInfoDao jobInfoDao;

    public void save (JobInfo jobInfo){
        JobInfo param=new JobInfo();
        param.setUrl(jobInfo.getUrl());
        param.setTime(jobInfo.getTime());
        List<JobInfo> jobInfoList = jobInfoDao.findAll(param);

        if(jobInfoList.size()==0){
            jobInfoDao.save(jobInfo);
        }


    }
}

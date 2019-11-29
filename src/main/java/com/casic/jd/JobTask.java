package com.casic.jd;

import com.casic.jd.bean.JobInfo;
import com.casic.jd.service.JobInfoService;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

@Component
public class JobTask implements PageProcessor {
    @Autowired
    JobInfoService jobInfoService;
//保定java
//    private String url="https://search.51job.com/list/160400,000000,0000,01%252C32,9,99,java,2,1.html?" +
//            "lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&" +
//            "providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&" +
//            "line=&specialarea=00&from=&welfare=";
//北京java计算机软件，互联网行业
    private String url="https://search.51job.com/list/010000,000000,0000,01%252C32,9,99,java,2,1.html?lang=c&stype=&postchannel=0000&workyear=99&cotype=99&degreefrom=99&jobterm=99&companysize=99&providesalary=99&lonlat=0%2C0&radius=-1&ord_field=0&confirmdate=9&fromType=&dibiaoid=0&address=&line=&specialarea=00&from=&welfare=";

    @Override

    public void process(Page page) {
        //解析页面
        List<Selectable> list = page.getHtml().css("#resultList .el").nodes();
        //判断获取到的集合是否为空
        if(list.size()==0){
            //如果为空，表示这是招聘详情，获取招聘详情信息，保存数据
            saveJobInfo(page);
        }else {
            //如果不为空，表示这是列表页,解析出详情页的url地址，放到任务队列中

            for(Selectable selectable:list){
//                Selectable a = selectable.css(".t1 a");
                String jobInfoUrl=selectable.links().toString();
                System.out.println(jobInfoUrl);
                page.addTargetRequest(jobInfoUrl);
            }
            //获取下一页的url地址放到任务队列中
            String nextPageUrl=page.getHtml().css(".bk").nodes().get(1).links().toString();
            page.addTargetRequest(nextPageUrl);
        }
        System.out.println("===========结束============");
    }

    private void saveJobInfo(Page page) {
        //创建招聘详情对象
        JobInfo jobInfo=new JobInfo();
        //解析页面
        Html html=page.getHtml();
        String companyName=html.css(".cname a","title").toString();
        jobInfo.setCompanyName(companyName);
//获取公司地址
        String companyAddr= Jsoup.parse(html.css(".bmsg .fp").nodes().get(html.css(".bmsg .fp").nodes().size()-1).toString()).text();
        jobInfo.setCompanyAddr(companyAddr);
        //获取公司信息
        String companyInfo=Jsoup.parse(html.css(".tmsg").toString()).text();
        jobInfo.setCompanyInfo(companyInfo);
        String jobName=Jsoup.parse(html.css(".cn h1").toString()).text();
        jobInfo.setJobName(jobName);
        String jobAddr=html.css(".ltype","title").toString();
       // jobAddr=jobAddr.split("\\|")[0];
        jobInfo.setJobAddr(jobAddr.split("\\|")[0].trim());
        //北京-西城区  |  8-9年经验  |  大专  |  招1人  |  11-29发布
        String msg=Jsoup.parse(html.css(".job_msg").toString()).text();
        jobInfo.setJobInfo(msg);
        String salaryStr=Jsoup.parse(html.css(".cn strong").toString()).text();
        Double[] salaryMinAndMax=parseSalary(salaryStr);
        jobInfo.setSalaryMin(salaryMinAndMax[0]);
        jobInfo.setSalaryMax(salaryMinAndMax[1]);
        jobInfo.setUrl(page.getUrl().toString());
        jobInfo.setTime(jobAddr.split("\\|")[4].replace("发布","").trim());

        //把结果保存起来
        page.putField("jobInfo",jobInfo);
    }

    private Site site=Site.me()
//                           .setCharset("utf8")
                           .setCharset("gbk")
                           .setTimeOut(10*1000)
                           .setRetrySleepTime(3000)
                           .setRetryTimes(3);

            ;
    @Override
    public Site getSite() {
        return this.site;
    }

    /**
     * initialDelay当任务启动后，等待多久执行方法
     * fixedDelay 每隔多久执行一次
     */
    @Scheduled(initialDelay = 1000,fixedDelay =100*1000)
    public void process(){
        Spider.create(new JobTask())
              .addUrl(url)
              .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
              .thread(10)
              .run();
    }
    public Double[] parseSalary(String salaryStr){
//        String moneyPreMonth=salaryStr.substring(salaryStr.length()-3,salaryStr.length());
        String min_Max=salaryStr.substring(0,salaryStr.length()-3);
        String[] salaryMinAndMaxStr= min_Max.split("-");
        Double[] salaryMinAndMax=null;
//        if("万/月".equals(moneyPreMonth)){
        if(salaryStr.endsWith("万/月")){
            salaryMinAndMax= new Double[]{(Double.parseDouble(salaryMinAndMaxStr[0])) * 10000, Double.parseDouble(salaryMinAndMaxStr[1]) * 10000};
        }else if(salaryStr.endsWith("千/月")){
            salaryMinAndMax= new Double[]{(Double.parseDouble(salaryMinAndMaxStr[0])) * 10000, Double.parseDouble(salaryMinAndMaxStr[1]) * 10000};
        }
        return salaryMinAndMax;
    }
}

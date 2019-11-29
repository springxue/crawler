package com.casic.webMagic.test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;

public class JobProcess implements PageProcessor {
    /**
     * 解析页面
     * @param page
     */
    @Override
    public void process(Page page) {
        //解析返回的数据page，并且把解析的结果放到resultItems中
        page.putField("div",page.getHtml().css(".mod_container ").all());
    }


    private Site site=Site.me();
    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new JobProcess()).addUrl("https://kuaibao.jd.com/")//设置爬取数据的页面
                .run();//执行爬虫
    }
}

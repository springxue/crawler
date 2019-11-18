package com.casic.jd;

import com.casic.jd.bean.Item;
import com.casic.jd.dao.ItemDao;
import com.casic.jd.utils.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ItemTask {
    @Autowired
    private HttpUtils httpUtils;
    @Autowired
    private ItemDao itemDao;
    //当下载任务完成后，间隔多长时间进行下一次的任务
    @Scheduled(fixedDelay = 100*1000)
    public void itemTask() throws Exception{
        //声明需要解析的初始地址
        String url="https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&wq=%E6%89%8B%E6%9C%BA&s=1&click=0&page=";
       //按照页面对手机的搜索结果进行遍历
        for(int i=1;i<10;i=i+2){

            String html = httpUtils.doGetHtml(url+i);
            //解析页面，获取商品数据并存储
           parse(html);
        }
        System.out.println("手机数据抓取完成");
    }

    /**
     *  解析页面，获取商品数据并存储
     * @param html
     */
    private void parse(String html) throws Exception {
        //解析html获取Document对象
        Document document= Jsoup.parse(html);
        System.out.println(document.toString());
        //获取spu
        Elements spuElements=document.select(".gl-item");
        for(Element spuElement:spuElements){
            //获取spu
          Long spu=Long.parseLong(spuElement.attr("data-spu"));
          Elements skuElements=spuElement.select("li.ps-item > a > img");
          for(Element skuElement:skuElements){
              //获取sku
              Long sku = Long.parseLong(skuElement.attr("data-sku"));
              //根据sku查询商品数据，如果商品已存在，就跳过，不保存
              Item item=new Item();
              item.setSku(sku);
              List<Item> itemList = itemDao.findAll(item);
              if(itemList.size()>0){
                  continue;
              }else {
                  //设置商品的spu
                  item.setSpu(spu);
                  //获取商品的详情url
                  String itemUrl="https://item.jd.com/"+sku+".html";
                  item.setUrl(itemUrl);
                  //获取商品的图片
                  String picUrl="https:"+skuElement.attr("data-lazy-img");
                  picUrl=picUrl.replace("n9","n1");
                  String picName=httpUtils.doGetImg(picUrl);
                  item.setPic(picName);
//                  //获取商品的价格

//                  item.setPrice();
//                  item.setTitle();
                  item.setCreated(new Date());
                  itemDao.save(item);
              }


          }
        }
    }
}

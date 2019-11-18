package com.casic.jd.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Component
public class HttpUtils {
    //httpClient连接池
    private PoolingHttpClientConnectionManager cm;
    public HttpUtils(){
        this.cm=new PoolingHttpClientConnectionManager();
        //设置最大连接数
        this.cm.setMaxTotal(100);
        //设置每个主机的最大连接数
        this.cm.setDefaultMaxPerRoute(10);
    }

    /**
     * 根据请求地址下载页面数据
     * @param url
     * @return 页面数据
     */
    public String doGetHtml(String url) {
        //获取HttpClient对象
        CloseableHttpClient httpClient= HttpClients.custom().setConnectionManager(this.cm).build();
        //创建httpGet请求对象，设置url地址
        HttpGet httpGet=new HttpGet(url);
        //设置请求头
        httpGet.setHeader("Accept", "Accept text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        httpGet.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate");
        httpGet.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
        httpGet.setHeader("Connection", "keep-alive");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");

        //设置请求信息
        httpGet.setConfig(getConfig());
        CloseableHttpResponse response=null;
        try {
            //使用HttpClient发起请求，获取响应
            response=httpClient.execute(httpGet);

            //解析响应，返回结果
            if(response.getStatusLine().getStatusCode()==200){
                if(response.getEntity()!=null){

                    String content = EntityUtils.toString(response.getEntity(), "utf8");
                    return content;
                }
            }else {

                return "";

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }


    /**
     * 下载图片
     * @param url
     * @return 图片名称
     */
    public String doGetImg(String url){
        //获取HttpClient对象
        CloseableHttpClient httpClient= HttpClients.custom().setConnectionManager(this.cm).build();
        //创建httpGet请求对象，设置url地址
        HttpGet httpGet=new HttpGet(url);
        //设置请求信息
        httpGet.setConfig(getConfig());
        CloseableHttpResponse response=null;
        try {
            //使用HttpClient发起请求，获取响应
            response=httpClient.execute(httpGet);

            //解析响应，返回结果
            if(response.getStatusLine().getStatusCode()==200){
                if(response.getEntity()!=null){
                    //下载图片
                    //获取图片后缀
                    String extName=url.substring(url.lastIndexOf("."));
                    //创建图片名，重命名图片
                    String picName= UUID.randomUUID().toString()+extName;
                    //下载图片
                    //声明OutPutStream
                    OutputStream outputStream=new FileOutputStream(new File("G:\\pictures\\"+picName));
                    response.getEntity().writeTo(outputStream);
                    return picName;
                }
            }else {

                return "";

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }
//设置请求信息
    private RequestConfig getConfig(){
        RequestConfig config=RequestConfig.custom()
                            .setConnectTimeout(1000)//创建连接的最长时间
                            .setConnectionRequestTimeout(500)//获取连接的最长时间
                            .setSocketTimeout(10000)//数据传输最长时间
                            .build();
        return config;
    }
}

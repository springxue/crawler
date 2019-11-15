package com.casic.jd.utils;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.stereotype.Component;

@Component
public class HttpUtils {
    //httpClient连接池
    private PoolingHttpClientConnectionManager cm;
    public HttpUtils(){
        this.cm=new PoolingHttpClientConnectionManager();
    }

    /**
     * 根据请求地址下载页面数据
     * @param url
     * @return 页面数据
     */
    public String doGetHtml(String url){
        return null;
    }

    /**
     * 下载图片
     * @param url
     * @return 图片名称
     */
    public String doGetImage(String url) {
        return null;
    }

}

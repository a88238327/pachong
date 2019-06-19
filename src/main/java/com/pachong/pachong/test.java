package com.pachong.pachong;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class test {
    public static String getURLContent(String requestUrl, Map<String, Object> data, String method) throws IOException {
        HttpURLConnection conn = null;
        BufferedReader br = null;
        StringBuffer sb = new StringBuffer();
        String content = null;
//GET请求的url链接
        if (null == method || "GET" == method) {
            requestUrl = requestUrl + "?" + urlEncode(data);
        }

        try {

            URL url = new URL(requestUrl);
            conn = (HttpURLConnection) url.openConnection();

//设置请求方式
            if ("GET" == method || null == method) {
                conn.setRequestMethod("GET");
            } else {
                conn.setRequestMethod("POST");
//使用URL连接输出
                conn.setDoOutput(true);
            }
//设置请求内核

            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36");
//设置不使用缓存
            conn.setUseCaches(false);
//设置链接超时时间,毫秒为单位
            conn.setConnectTimeout(1000);
//设置读取超时时间，毫秒为单位
            conn.setReadTimeout(1000);
//设置当前链接是否自动处理重定向。setFollowRedirects设置所有的链接是否自动处理重定向
            conn.setInstanceFollowRedirects(false);
//开启链接
            conn.connect();
//处理post请求时的参数
            if (null != data && "POST" == method) {
                DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                out.writeBytes(urlEncode(data));
            }
//获取字符输入流
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String strContent = null;
            while ((strContent = br.readLine()) != null) {
                sb.append(strContent);
            }
            content = sb.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
//关闭流和链接
            if (null != br) {
                br.close();
            }
            if (null != conn) {
                conn.disconnect();
            }
        }

        return content;
    }
//将map型转为请求参数型


    public static String urlEncode(Map<String, ?> data) {
        if (data == null) {
            return "";
        } else {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, ?> i : data.entrySet()) {
                try {
                    sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) {

        Map<String, Object> data = new HashMap<String, Object>();

        data.put("1", "111");
        data.put("2", "222");
        String str="https://detail.tmall.com/item.htm?id=525981114588";
        try {

            String content = getURLContent(str, data, "GET");

            System.out.println(content);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

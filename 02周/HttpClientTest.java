package com.example.nio;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientTest {
    public static void main(String[] args) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://localhost:8801");
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            System.out.println("请求返回状态：" + response.getStatusLine());
            System.out.println("请求返回类型：" + entity.getContentType());
            System.out.println("请求返回内容：" + EntityUtils.toString(entity));
        } catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (response != null){
                    response.close();
                }
                httpClient.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }


    }
}

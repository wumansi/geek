package com.sisi.rpccore.proxy;

import com.alibaba.fastjson.JSON;
import com.sisi.rpccore.Netty.client.RpcNettyClientSync;
import com.sisi.rpccore.api.RpcRequest;
import com.sisi.rpccore.api.RpcResponse;
import com.sisi.rpccore.discovery.DiscoveryClient;
import com.sisi.rpccore.filter.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class RpcInvocationHandler implements InvocationHandler, MethodInterceptor {
    private final Class<?> serviceClass;
    private final String group;
    private final String version;
    private DiscoveryClient discoveryClient = DiscoveryClient.getInstance();
    private final List<String> tags = new ArrayList<>();
    private int retryTime = 0;

    <T> RpcInvocationHandler(Class<T> serviceClass){
        this.serviceClass = serviceClass;
        this.group = "default";
        this.version = "default";
        System.out.println("Client service Class reflect:" + group + ":" + version);
    }

    <T> RpcInvocationHandler(Class<T> serviceClass, String group, String version){
        this.serviceClass = serviceClass;
        this.group = group;
        this.version = version;
        System.out.println("Client service Class reflect:" + group + ":" + version);
    }

    <T> RpcInvocationHandler(Class<T> serviceClass, String group, String version, List<String> tags){
        this.serviceClass = serviceClass;
        this.group = group;
        this.version = version;
        this.tags.addAll(tags);
        System.out.println("Client service Class reflect:" + group + ":" + version);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return process(serviceClass, method, args);
        } catch (Exception e){
            if (retryTime < Retry.getRetryLimit()){
                retryTime += 1;
                invoke(proxy, method, args);
            } else {
                retryTime = 0;
            }
        }
        return null;
    }

    /**
     * 发送请求到服务端.
     * 获取返回结果后序列化成对象，返回
     * @param serviceClass service name
     * @param method service method
     * @param args method params
     * @return object
     */
    private Object process(Class<?> serviceClass, Method method, Object[] args) {
        log.info("Client proxy instance method invoke");
        log.info("build rpc request");
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceClass(serviceClass.getName());
        rpcRequest.setMethod(method.getName());
        rpcRequest.setArgs(args);
        rpcRequest.setGroup(group);
        rpcRequest.setVersion(version);

        // 从DiscoveryClient中获取某个Provider的请求地址
        String url = null;
        try {
            url = discoveryClient.getProviders(serviceClass.getName(), group, version, tags, method.getName());
        } catch (Exception e){
            e.printStackTrace();
        }
        if (url == null){
            System.out.println("未找到对应服务提供者");
            return null;
        }

        log.info("client send request to Server");
        RpcResponse rpcResponse;
        try {
            rpcResponse = RpcNettyClientSync.getInstance().getResponse(rpcRequest, url);
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        log.info("client receive response object");
        assert rpcResponse != null;
        if (!rpcResponse.getStatus()){
            log.info("client receice data exception");
            rpcResponse.getException().printStackTrace();
            return null;
        }

        // 序列化成对象返回
        log.info("client response:" + rpcResponse.getResult());
        return JSON.parse(rpcResponse.getResult().toString());
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        try {
            return process(serviceClass, method, args);
        } catch (Exception e){
            if (retryTime < Retry.getRetryLimit()){
                retryTime += 1;
                intercept(o, method, args, methodProxy);
            } else {
                retryTime = 0;
            }
        }
        return null;
    }
}

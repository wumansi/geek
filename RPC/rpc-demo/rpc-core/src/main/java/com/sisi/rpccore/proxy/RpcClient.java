package com.sisi.rpccore.proxy;

import com.google.common.base.Joiner;
import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建代理类
 */
public class RpcClient {
    private ConcurrentHashMap<String, Object> proxyCache = new ConcurrentHashMap<>();

    private Boolean isExist(String className){
        return proxyCache.containsKey(className);
    }

    private Object getProxy(String className){
        return proxyCache.get(className);
    }

    private void add(String className, Object proxy){
        proxyCache.put(className, proxy);
    }

    public <T> T create(Class<T> serviceClass){
        String invoker = serviceClass.getName();
        if (!isExist(invoker)){
            add(invoker, newProxy(serviceClass));
        }
        return (T) getProxy(invoker);
    }

    public <T> T create(Class<T> serviceClass, String group, String version){
        String invoker = Joiner.on(":").join(serviceClass.getName(), group, version);
        if (!isExist(invoker)){
            add(invoker, newProxy(serviceClass, group, version));
        }
        return (T) getProxy(invoker);
    }

    public <T> T create(Class<T> serviceClass, String group, String version, List<String> tags){
        String invoker = Joiner.on(":").join(serviceClass.getName(), group, version, tags.toString());
        if (!isExist(invoker)){
            add(invoker, newProxy(serviceClass, group, version, tags));
        }
        return (T) getProxy(invoker);
    }

    @SneakyThrows
    private <T> T newProxy(Class<T> serviceClass, String group, String version, List<String> tags) {
        return (T) new ByteBuddy().subclass(Object.class)
                .implement(serviceClass)
                .intercept(InvocationHandlerAdapter.of(new RpcInvocationHandler(serviceClass, group, version, tags)))
                .make()
                .load(RpcClient.class.getClassLoader())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }

    @SneakyThrows
    private <T> T newProxy(Class<T> serviceClass, String group, String version) {
        return (T) new ByteBuddy().subclass(Object.class)
                .implement(serviceClass)
                .intercept(InvocationHandlerAdapter.of(new RpcInvocationHandler(serviceClass, group, version)))
                .make()
                .load(RpcClient.class.getClassLoader())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }

    @SneakyThrows
    private <T> T newProxy(Class<T> serviceClass) {
        return (T) new ByteBuddy().subclass(Object.class)
                .implement(serviceClass)
                .intercept(InvocationHandlerAdapter.of(new RpcInvocationHandler(serviceClass)))
                .make()
                .load(RpcClient.class.getClassLoader())
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }
}

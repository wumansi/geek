package com.sisi.rpccore.discovery;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.sisi.rpccore.api.ProviderInfo;
import com.sisi.rpccore.balance.LoadBalance;
import com.sisi.rpccore.balance.loadbalance.WeightBalance;
import com.sisi.rpccore.filter.FilterLine;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 服务发现客户端，继承ZK客户端.
 */
@Slf4j
public class DiscoveryClient extends ZookeeperClient{
    //懒汉枚举单例
    private enum EnumSingleton {
        INSTANCE;
        private DiscoveryClient instance;
        EnumSingleton() {
            instance = new DiscoveryClient();
        }
        public DiscoveryClient getInstance(){
            return instance;
        }
    }

    public static DiscoveryClient getInstance(){
        return EnumSingleton.INSTANCE.getInstance();
    }
    /**
     * Provider缓存列表
     * service:group:version -> Instance list
     */
    private Map<String, List<ProviderInfo>> providerCache = new HashMap<>();
    private final ServiceDiscovery<ProviderInfo> serviceDiscovery;
    private final CuratorCache resourcesCache;
    private LoadBalance balance = new WeightBalance();

    public DiscoveryClient() {
        serviceDiscovery = ServiceDiscoveryBuilder.builder(ProviderInfo.class)
                .client(client)
                .basePath("/" + REGISTER_ROOT_PATH)
                .build();

        try {
            serviceDiscovery.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            getAllProviders();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.resourcesCache = CuratorCache.build(this.client, "/");

    }

    /**
     * 从zookeeper客户端获取所有的Provider列表，保存（缓存）下来
     * @throws Exception
     */
    private void getAllProviders() throws Exception {
        log.info("========init : get all provider");

        Collection<String> serviceNames = serviceDiscovery.queryForNames();
        for (String serviceName : serviceNames){
            Collection<ServiceInstance<ProviderInfo>> instances = serviceDiscovery.queryForInstances(serviceName);

            for (ServiceInstance<ProviderInfo> instance : instances){
                String url = "http://" + instance.getAddress() + ":" + instance.getPort();
                ProviderInfo providerInfo = instance.getPayload();
                providerInfo.setId(instance.getId());
                providerInfo.setUrl(url);

                List<ProviderInfo> providerInfoList = providerCache.getOrDefault(instance.getName(), new ArrayList<>());
                providerInfoList.add(providerInfo);
                providerCache.put(instance.getName(), providerInfoList);
                log.info("add provider:" + instance.toString());
            }
        }
    }

    /**
     * 根据传入接口名称、分组、版本返回经过路由、负载均衡后的一个Provider服务器地址
     * @param service
     * @param group
     * @param version
     * @param tags
     * @param methodName
     * @return
     */
    public String getProviders(String service, String group, String version, List<String> tags, String methodName) {
        String provider = Joiner.on(":").join(service, group, version);
        if (!providerCache.containsKey(provider) || providerCache.get(provider).isEmpty()){
            return null;
        }
        List<ProviderInfo> providers = FilterLine.filter(providerCache.get(provider), tags);
        if (providers.isEmpty()){
            return null;
        }
        return balance.select(providers,service, methodName);
    }
    /**
     * 监听provider的更新
     */
    private void watchResource() {
        CuratorCacheListener listener = CuratorCacheListener.builder()
                .forCreates(this::addHandler)
                .forChanges(this::changeHandler)
                .forDeletes(this::deleteHandler)
                .forInitialized(()->{log.info("initialized");})
                .build();
        resourcesCache.listenable().addListener(listener);
        resourcesCache.start();
    }

    private void addHandler(ChildData node) {
        if (providerDataEmpty((node))){
            return;
        }
        updateProvider(node);
    }
    private void changeHandler(ChildData oldnode, ChildData newNode) {
        if (providerDataEmpty((newNode))){
            return;
        }
        updateProvider(newNode);
    }
    public void deleteHandler(ChildData newNode) {
        if (providerDataEmpty((newNode))){
            return;
        }

        String jsonValue = new String(newNode.getData(), StandardCharsets.UTF_8);
        JSONObject instance = (JSONObject) JSONObject.parse(jsonValue);
        String provider = instance.get("name").toString();
        int deleteIndex = -1;
        for (int i=0;i<providerCache.get(provider).size();i++){
            if (providerCache.get(provider).get(i).getId().equals(instance.get("id").toString())){
                deleteIndex = i;
            }
            break;
        }
        if (deleteIndex != -1){
            providerCache.get(provider).remove(deleteIndex);
        }
        log.info("=============== delete provider end=========");
    }
    private void updateProvider(ChildData newNode) {
        String jsonValue = new String(newNode.getData(), StandardCharsets.UTF_8);
        JSONObject instance = (JSONObject) JSONObject.parse(jsonValue);

        String url = "http://" + instance.get("address") + ":" + instance.get("port");
        ProviderInfo providerInfo = JSON.parseObject(instance.get("payload").toString(), ProviderInfo.class);
        providerInfo.setId(instance.get("id").toString());
        providerInfo.setUrl(url);

        List<ProviderInfo> providerInfoList = providerCache.getOrDefault(instance.get("name").toString(), new ArrayList<>());
        providerInfoList.add(providerInfo);
        providerCache.put(instance.get("name").toString(), providerInfoList);
    }

    private boolean providerDataEmpty(ChildData node){
        return node.getData().length == 0;
    }
}

package com.sisi.rpccore.discovery;

import com.google.common.base.Joiner;
import com.sisi.rpccore.api.ProviderInfo;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务发现器：用于注册provider
 */
public class DiscoveryServer extends ZookeeperClient{
    private List<ServiceDiscovery<ProviderInfo>> discoveryList = new ArrayList<>();

    public DiscoveryServer(){

    }

    /**
     * 生成Provider的相关信息，注册到ZK中
     * @param service Service impl name
     * @param group group
     * @param version version
     * @param port service listen port
     * @param tags route tags
     * @param weight load balance weight
     * @throws Exception exception
     */
    public void registerService(String service, String group, String version, int port, List<String> tags,
                                int weight) throws Exception {
        ProviderInfo provider = new ProviderInfo(null, null, tags, weight);

        ServiceInstance<ProviderInfo> instance = ServiceInstance.<ProviderInfo>builder()
                .name(Joiner.on(":").join(service,group,version))
                .port(port)
                .address(InetAddress.getLocalHost().getHostAddress())
                .payload(provider)
                .build();
        JsonInstanceSerializer<ProviderInfo> serializer= new JsonInstanceSerializer<>(ProviderInfo.class);
        ServiceDiscovery<ProviderInfo> discovery = ServiceDiscoveryBuilder.builder(ProviderInfo.class)
                .client(client)
                .basePath(REGISTER_ROOT_PATH)
                .thisInstance(instance)
                .serializer(serializer)
                .build();
        discovery.start();

        discoveryList.add(discovery);
    }

    public void close() throws IOException{
        for (ServiceDiscovery<ProviderInfo> discovery: discoveryList){
            discovery.close();
        }
        client.close();
    }
}

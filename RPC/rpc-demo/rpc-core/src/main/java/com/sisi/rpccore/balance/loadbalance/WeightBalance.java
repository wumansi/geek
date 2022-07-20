package com.sisi.rpccore.balance.loadbalance;

import com.sisi.rpccore.api.ProviderInfo;

import java.util.List;
import java.util.Random;

/**
 * 加权负载均衡
 */
public class WeightBalance extends AbstractLoadBalance {
    @Override
    public String select(List<ProviderInfo> providers, String serviceName, String method) {
        int totalWeight = 0;
        for (ProviderInfo provider : providers){
            totalWeight += provider.getWeight();
        }

        int random = new Random().nextInt(totalWeight);
        for (ProviderInfo provider : providers){
            random -= provider.getWeight();
            if (random < 0){
                provider.getUrl();
            }
        }

        return providers.get(providers.size()-1).getUrl();
    }
}

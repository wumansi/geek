package com.sisi.rpccore.filter;

import com.sisi.rpccore.api.ProviderInfo;

import java.util.ArrayList;
import java.util.List;

public class TagFilter implements RpcFilter{
    @Override
    public List<ProviderInfo> filter(List<ProviderInfo> providers, List<String> tags) {
        if (tags.isEmpty()){
            return providers;
        }
        List<ProviderInfo> newProviders = new ArrayList<>(providers.size());

        for (ProviderInfo provider : providers){
            for (String tag : tags){
                if (provider.getTags().contains(tag)){
                    newProviders.add(provider);
                    break;
                }
            }
        }
        return newProviders;
    }
}

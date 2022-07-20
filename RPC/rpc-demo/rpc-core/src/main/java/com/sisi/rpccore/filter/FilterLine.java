package com.sisi.rpccore.filter;

import com.sisi.rpccore.api.ProviderInfo;

import java.util.ArrayList;
import java.util.List;

public class FilterLine {
    private static Boolean isInit = false;
    private static List<RpcFilter> rpcFilters = new ArrayList<>();

    private static void init(){
        rpcFilters.add(new TagFilter());
    }

    public static List<ProviderInfo> filter(List<ProviderInfo> providers, List<String> tags){
        if (!isInit){
            init();
            isInit = true;
        }

        List<ProviderInfo> filterResult = new ArrayList<>(providers);
        for (RpcFilter filter : rpcFilters){
            filterResult = filter.filter(filterResult, tags);
        }

        return filterResult;
    }
}

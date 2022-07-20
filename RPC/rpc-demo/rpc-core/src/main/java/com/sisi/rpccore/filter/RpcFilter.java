package com.sisi.rpccore.filter;

import com.sisi.rpccore.api.ProviderInfo;

import java.util.List;

public interface RpcFilter {
    List<ProviderInfo> filter(List<ProviderInfo> providers, List<String> tags);
}

package com.sisi.rpccore.balance;

import com.sisi.rpccore.api.ProviderInfo;

import java.util.List;

/**
 * 负载均衡
 */
public interface LoadBalance {
    /**
     * 从当前Provider列表中，通过负载均衡，返回其中一个Provider的请求地址
     * @param providers
     * @param serviceName
     * @param method
     * @return url
     */
    String select(List<ProviderInfo> providers, String serviceName, String method);
}

package com.sisi.rpccore.api;

import lombok.Data;

import java.util.List;

@Data
public class ProviderInfo {
    String id;
    String url;
    List<String> tags;
    Integer weight;

    public ProviderInfo(){}
    public ProviderInfo(String id, String url, List<String> tags, int weight){
        this.id = id;
        this.url = url;
        this.tags = tags;
        this.weight = weight;
    }

}

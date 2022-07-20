package com.sisi.rpccore.api;

import lombok.Data;

@Data
public class RpcRequest {
    private String serviceClass;
    private String method;
    private Object[] args;

    private String group;
    private String version;
}

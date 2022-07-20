package com.sisi.rpccore.Netty.common;

import lombok.Data;

@Data
public class RpcProtocol {

    private int len;

    private byte[] content;
}

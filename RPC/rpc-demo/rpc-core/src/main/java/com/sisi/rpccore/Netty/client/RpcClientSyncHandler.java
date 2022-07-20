package com.sisi.rpccore.Netty.client;

import com.alibaba.fastjson.JSON;
import com.sisi.rpccore.Netty.common.RpcProtocol;
import com.sisi.rpccore.api.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

/**
 * 响应返回处理器.
 */
@Slf4j
public class RpcClientSyncHandler extends SimpleChannelInboundHandler<RpcProtocol>{

    private CountDownLatch latch;
    private RpcResponse response;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol msg) throws Exception {
        log.info("Netty Client receive message:");
        log.info("message lenth:" + msg.getLen());
        log.info("message content:"  + new String(msg.getContent(), CharsetUtil.UTF_8));

        String json = new String(msg.getContent(), CharsetUtil.UTF_8);
        RpcResponse rpcResponse = JSON.parseObject(json, RpcResponse.class);
        response = rpcResponse;
        latch.countDown();
    }

    /**
     * 锁初始化.
     * @param latch
     */
    void setLatch(CountDownLatch latch){
        this.latch = latch;
    }

    /**
     * 阻塞等待结果返回.
     * @return 服务器响应
     * @throws InterruptedException
     */
    RpcResponse getResponse() throws InterruptedException {
        latch.await();
        return response;
    }
}

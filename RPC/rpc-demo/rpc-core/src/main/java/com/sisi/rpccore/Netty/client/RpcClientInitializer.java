package com.sisi.rpccore.Netty.client;

import com.sisi.rpccore.Netty.common.RpcDecoder;
import com.sisi.rpccore.Netty.common.RpcEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;


public class RpcClientInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast("message encoder", new RpcEncoder());
        pipeline.addLast("message decoder", new RpcDecoder());
        pipeline.addLast("clientHandler", new RpcClientSyncHandler());
    }
}

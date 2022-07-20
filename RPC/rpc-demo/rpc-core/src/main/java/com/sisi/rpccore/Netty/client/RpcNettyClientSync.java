package com.sisi.rpccore.Netty.client;

import com.alibaba.fastjson.JSON;
import com.sisi.rpccore.Netty.common.RpcProtocol;
import com.sisi.rpccore.api.RpcRequest;
import com.sisi.rpccore.api.RpcResponse;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.shaded.com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * netty 客户端
 */
@Slf4j
public class RpcNettyClientSync {
    private enum EnumSingle{
        /**
         * 枚举实例.
         */
        INSTANCE;
        private RpcNettyClientSync instance;
        EnumSingle(){
            this.instance = new RpcNettyClientSync();
        }
        public RpcNettyClientSync getInstance(){
            return instance;
        }
    }
    public static RpcNettyClientSync getInstance(){
        return EnumSingle.INSTANCE.getInstance();
    }

    private ConcurrentHashMap<String, Channel> channelPool = new ConcurrentHashMap<>();
    private EventLoopGroup clientGroup = new NioEventLoopGroup(new ThreadFactoryBuilder().setNameFormat("client work-%d").build());

    public RpcResponse getResponse(RpcRequest rpcRequest, String url) throws URISyntaxException, InterruptedException {
        RpcProtocol request = converNettyRequest(rpcRequest);

        URI uri = new URI(url);
        String cacheKey = uri.getHost() + ":" + uri.getPort();
        if (channelPool.contains(cacheKey)){
            Channel channel = channelPool.get(cacheKey);
            if (!channel.isOpen() || !channel.isActive() || !channel.isWritable()){
                log.debug("channel can not reuse");
            }else {
                try {
                    RpcClientSyncHandler handler = new RpcClientSyncHandler();
                    handler.setLatch(new CountDownLatch(1));
                    channel.pipeline().replace("clientHandler", "clientHandler", handler);
                    channel.writeAndFlush(request).sync();
                    return handler.getResponse();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    channel.close();
                    channelPool.remove(cacheKey);
                }
            }
        }
        RpcClientSyncHandler handler = new RpcClientSyncHandler();
        handler.setLatch(new CountDownLatch(1));
        Channel channel = createChannel(uri.getHost(), uri.getPort());
        channel.pipeline().replace("clientHandler", "clientHandler", handler);
        channelPool.put(cacheKey, channel);

        channel.writeAndFlush(request).sync();
        return handler.getResponse();
    }

    private Channel createChannel(String address, int port) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup)
                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.AUTO_CLOSE,true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());
        return bootstrap.connect(address, port).sync().channel();
    }

    private RpcProtocol converNettyRequest(RpcRequest rpcRequest) {
        RpcProtocol request = new RpcProtocol();
        String requestJson = JSON.toJSONString(rpcRequest);
        request.setLen(requestJson.getBytes(CharsetUtil.UTF_8).length);
        request.setContent(requestJson.getBytes(CharsetUtil.UTF_8));
        return request;
    }
}

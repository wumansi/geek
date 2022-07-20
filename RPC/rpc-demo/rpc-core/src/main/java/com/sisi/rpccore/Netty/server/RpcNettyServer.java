package com.sisi.rpccore.Netty.server;

import com.sisi.rpccore.Netty.common.RpcDecoder;
import com.sisi.rpccore.Netty.common.RpcEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * netty服务端启动类.
 */
@Slf4j
public class RpcNettyServer {

    private EventLoopGroup boss;
    private EventLoopGroup worker;
    private int port;

    public RpcNettyServer(int port){
        this.port = port;
    }

    public void destroy(){
        worker.shutdownGracefully();
        boss.shutdownGracefully();
    }

    public void run() throws InterruptedException {
        boss = new NioEventLoopGroup(1);
        worker = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer() {
                    protected void initChannel(Channel channel){
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast("message encoder", new RpcEncoder())
                                .addLast("message decoder", new RpcDecoder())
                                .addLast("message handler", new RpcServerHandler());
                    }
                });
        Channel channel = serverBootstrap.bind(port).sync().channel();
        log.info("Netty server listen in port:" + port);
        channel.closeFuture().sync();
    }
}

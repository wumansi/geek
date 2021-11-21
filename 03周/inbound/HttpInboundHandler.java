package com.example.gateway.inbound;

import com.example.gateway.filter.HeaderHttpRequestFilter;
import com.example.gateway.filter.HttpRequestFilter;
import com.example.gateway.outbound.OkhttpOutboundHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter{
    private static Logger logger = LoggerFactory.getLogger(HttpInboundHandler.class);
    private final List<String> proxyServer;
//    private HttpOutboundHandler handler;
    private OkhttpOutboundHandler handler;
//    private HttpClientOutboundHandler handler;
//    private AsyncOkhttpOutboundHandler handler;
    private HttpRequestFilter filter = new HeaderHttpRequestFilter();
//    private LogHttpRequestFilter filterlog = new LogHttpRequestFilter();

    public HttpInboundHandler(List<String> proxyServer) {
        this.proxyServer = proxyServer;
        this.handler = new OkhttpOutboundHandler(this.proxyServer);
//        this.handler = new HttpOutboundHandler(this.proxyServer);
//        this.handler = new HttpClientOutboundHandler(this.proxyServer);
//        this.handler = new AsyncOkhttpOutboundHandler(this.proxyServer);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            logger.info("接收到的请求uri为{}", uri);
            handler.handle(request, ctx, filter);
//            handler.handle(request, ctx, filterlog);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}

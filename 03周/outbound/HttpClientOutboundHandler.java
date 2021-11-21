package com.example.gateway.outbound;

import com.example.gateway.filter.HeaderHttpResponseFilter;
import com.example.gateway.filter.HttpResponseFilter;
import com.example.gateway.filter.LogHttpRequestFilter;
import com.example.gateway.router.HttpEndpointRouter;
import com.example.gateway.router.RandomHttpEnpointRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpClientOutboundHandler {
    private CloseableHttpClient httpClient;
    private ExecutorService proxyService;
    private List<String> backendUrls;
    private HttpResponseFilter filter = new HeaderHttpResponseFilter();
    private HttpEndpointRouter router = new RandomHttpEnpointRouter();


    public HttpClientOutboundHandler(List<String> backends){
        this.backendUrls = backends.stream().map(this::formatUrl).collect(Collectors.toList());
        int cores = Runtime.getRuntime().availableProcessors();
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        proxyService = new ThreadPoolExecutor(cores, cores, keepAliveTime, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueSize), new NameThreadFactory("proxyService"), handler);
        httpClient = HttpClients.createDefault();
    }

    private String formatUrl(String backend){
        return backend.endsWith("/")?backend.substring(0,backend.length()-1):backend;
    }

    public void handle(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final LogHttpRequestFilter filter){
        final String url = router.route(backendUrls) + inbound.uri();
        filter.filter(inbound, ctx);
        System.out.println("请求的路由地址为：" + url);
        proxyService.submit(() -> fetchGet(inbound, ctx, url));
    }

    private void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url){
        final HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(RequestConfig.custom().setConnectTimeout(1000).setSocketTimeout(1000).build());
        httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);
        httpGet.setHeader("mao", inbound.headers().get("mao"));
        CloseableHttpResponse response = null;
        DefaultFullHttpResponse fullresponse = null;
        try {
            response = httpClient.execute(httpGet);
            fullresponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(EntityUtils.toByteArray(response.getEntity())));
            fullresponse.headers().set("Content-Type", "application/json");
            fullresponse.headers().set("Content-Length", fullresponse.content().readableBytes());
            filter.filter(fullresponse);
            System.out.println("请求返回状态：" + response.getStatusLine());
        } catch (IOException e) {
            e.printStackTrace();
            fullresponse = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            ctx.close();
        } finally {
            if (inbound != null){
                if (!HttpUtil.isKeepAlive(inbound)){
                    ctx.write(fullresponse).addListener(ChannelFutureListener.CLOSE);
                } else {
                    ctx.write(fullresponse);
                }
            }
            ctx.flush();
        }
    }
}

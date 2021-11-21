package com.example.gateway.outbound;

import com.example.gateway.filter.HeaderHttpResponseFilter;
import com.example.gateway.filter.HttpRequestFilter;
import com.example.gateway.filter.HttpResponseFilter;
import com.example.gateway.router.HttpEndpointRouter;
import com.example.gateway.router.RandomHttpEnpointRouter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpUtil;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class AsyncOkhttpOutboundHandler {
    private ExecutorService proxyService;
    private List<String> backendUrls;
    private HttpResponseFilter filter = new HeaderHttpResponseFilter();
    private HttpEndpointRouter router = new RandomHttpEnpointRouter();
    public static OkHttpClient client = new OkHttpClient().newBuilder().build();


    public AsyncOkhttpOutboundHandler(List<String> backends) {
        this.backendUrls = backends.stream().map(this::formatUrl).collect(Collectors.toList());
        int cores = Runtime.getRuntime().availableProcessors();
        long keepAliveTime = 1000;
        int queueSize = 2048;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        proxyService = new ThreadPoolExecutor(cores, cores, keepAliveTime, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(queueSize), new NameThreadFactory("proxyService"), handler);

    }

    private String formatUrl(String backend){
        return backend.endsWith("/")?backend.substring(0,backend.length()-1):backend;
    }

    public void handle(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final HttpRequestFilter filter){
        final String url = router.route(backendUrls) + inbound.uri();
        filter.filter(inbound, ctx);
        System.out.println("请求的路由地址为：" + url);
        proxyService.submit(() -> handlerAsync(inbound, ctx, url));
    }

    private void handlerAsync(FullHttpRequest inbound, ChannelHandlerContext ctx, String url) {
        Request request = new Request.Builder().url(url).build();
        DefaultFullHttpResponse fullresponse = null;
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("request fail," + e.getLocalizedMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] bytes = response.body().string().getBytes();
                DefaultFullHttpResponse fullresponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(bytes));
                fullresponse.headers().set("Content-Type", "application/json");
                fullresponse.headers().set("Content-Length", fullresponse.content().readableBytes());
                filter.filter(fullresponse);
                if (inbound != null){
                    if (!HttpUtil.isKeepAlive(inbound)){
                        ctx.write(fullresponse).addListener(ChannelFutureListener.CLOSE);
                    } else {
                        ctx.write(fullresponse);
                    }
                    ctx.flush();
                }
            }
        });
    }

}

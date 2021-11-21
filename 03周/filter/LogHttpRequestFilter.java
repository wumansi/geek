package com.example.gateway.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class LogHttpRequestFilter implements HttpRequestFilter {
    private static Logger logger = LoggerFactory.getLogger(LogHttpRequestFilter.class);
    @Override
    public void filter(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
        logger.info("请求内容为：" + fullHttpRequest.content().toString(Charset.forName("UTF-8")));
    }
}

package com.example.gateway.filter;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

public class HeaderHttpResponseFilter implements HttpResponseFilter {
    @Override
    public void filter(FullHttpResponse response) {
        response.headers().set("sisi", "nio");
        response.setStatus(HttpResponseStatus.NO_CONTENT);
    }
}

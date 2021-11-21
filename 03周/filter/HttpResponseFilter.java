package com.example.gateway.filter;

import io.netty.handler.codec.http.FullHttpResponse;
import org.apache.http.HttpResponse;

public interface HttpResponseFilter {
    void filter(FullHttpResponse response);
}

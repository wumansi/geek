package com.sisi.rpccore.Netty.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Joiner;
import com.sisi.rpccore.Netty.common.RpcProtocol;
import com.sisi.rpccore.api.RpcRequest;
import com.sisi.rpccore.api.RpcResponse;
import com.sisi.rpccore.proxy.ProviderServiceManagement;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcProtocol>{
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol msg) throws Exception {
        log.info("message length" + msg.getLen());
        log.info("message content" + new String(msg.getContent(), CharsetUtil.UTF_8));

        //获取RpcProtocol中请求内容，反序列成对象
        String json = new String(msg.getContent(), CharsetUtil.UTF_8);
        RpcRequest request = JSON.parseObject(json, RpcRequest.class);
        log.info(request.toString());

        // 从缓存中获取对应实例对象（实现类），反射调用方法，获取结果
        RpcResponse response = invoke(request);
        // 返回结果给netty客户端
        RpcProtocol message = new RpcProtocol();
        String requestJson = JSON.toJSONString(response);
        message.setLen(requestJson.getBytes(CharsetUtil.UTF_8).length);
        message.setContent(requestJson.getBytes(CharsetUtil.UTF_8));
        channelHandlerContext.writeAndFlush(message).sync();
        log.info("return response to client end");
    }

    /**
     * 获取接口实现对应的bean,反射调用方法，返回结果
     * @param request
     * @return
     */
    private RpcResponse invoke(RpcRequest request){
        RpcResponse response = new RpcResponse();
        Object service = ProviderServiceManagement.getProviderService(request);

        String methodName = request.getMethod();
        Method method = Arrays.stream(service.getClass().getMethods()).filter(m -> methodName.equals(m.getName()))
                .findFirst().get();
        try {
            Object result = method.invoke(service, request.getArgs());
            log.info("server method invoke result:" + result.toString());
            response.setStatus(true);
            response.setResult(JSON.toJSONString(result, SerializerFeature.WriteClassName));
            return response;
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            response.setException(e);
            response.setStatus(false);
            return response;
        }
    }
}

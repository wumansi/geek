package com.sisi.rpccore.Netty.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定义解码器.
 */
public class RpcDecoder extends ByteToMessageDecoder{
    private int length = 0;
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() >= 4){
            if (length == 0){
                length = byteBuf.readInt();
            }
            if (byteBuf.readableBytes() < length){
                return;
            }
            byte[] content = new byte[length];
            if (byteBuf.readableBytes() >= length){
                byteBuf.readBytes(content);
                RpcProtocol rpcProtocol = new RpcProtocol();
                rpcProtocol.setLen(length);
                rpcProtocol.setContent(content);
                list.add(rpcProtocol);
            }
            length = 0;
        }
    }
}

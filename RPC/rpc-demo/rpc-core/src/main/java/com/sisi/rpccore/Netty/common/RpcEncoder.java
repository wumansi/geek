package com.sisi.rpccore.Netty.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义编码器.
 */
@Slf4j
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol>{
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcProtocol msg, ByteBuf byteBuf) throws Exception {
        log.info("netty rpc encode run");
        byteBuf.writeInt(msg.getLen());
        byteBuf.writeBytes(msg.getContent());
    }
}

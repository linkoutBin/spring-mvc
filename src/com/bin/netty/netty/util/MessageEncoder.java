package com.bin.netty.netty.util;

import com.bin.netty.netty.vo.MessageRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Object> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        try {
            MessageRequest request = (MessageRequest) o;
            System.out.println("原始数据：" + request.toString());
            System.out.println("this is messageEncoder");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

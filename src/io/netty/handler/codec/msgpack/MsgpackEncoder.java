package io.netty.handler.codec.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

public class MsgpackEncoder extends MessageToByteEncoder<Object> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        try {
            System.out.println("待发送的对象：" + o);
            MessagePack messagePack = new MessagePack();
            byte[] b = messagePack.write(o);
            byteBuf.writeBytes(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

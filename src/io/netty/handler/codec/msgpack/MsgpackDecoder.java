package io.netty.handler.codec.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        try {
            final byte[] array;
            final int length = byteBuf.readableBytes();
            System.out.println("接收到的码流长度:" + length);
            array = new byte[length];
            byteBuf.getBytes(byteBuf.readerIndex(), array, 0, length);
            MessagePack messagePack = new MessagePack();
            list.add(messagePack.read(array));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

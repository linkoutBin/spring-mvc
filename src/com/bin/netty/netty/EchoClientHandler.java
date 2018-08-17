package com.bin.netty.netty;

import com.bin.netty.netty.vo.MessageRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private int counter;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //String response = (String) msg;
        System.out.println("receive response from server " + ++counter + " :" + msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("start to send msg!");
        MessageRequest[] messageRequests = buildMessages();
        for (MessageRequest request : messageRequests) {
            //ctx.writeAndFlush(Unpooled.copiedBuffer("netty-netty$_", CharsetUtil.UTF_8));
            /*MessageRequest request = new MessageRequest();
            request.setMessage("hello");
            request.setMessageId("messageID:"+i);*/

            ctx.write(request);
        }
        ctx.flush();
        System.out.println("send is over");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private MessageRequest[] buildMessages() {
        int num = 20;
        MessageRequest[] requests = new MessageRequest[num];
        MessageRequest request;
        for (int i = 0; i < num; i++) {
            request = new MessageRequest();
            request.setMessage("message-" + i);
            request.setMessageId("MID-" + i);
            requests[i] = request;
        }
        return requests;
    }
}

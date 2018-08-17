package com.bin.netty.netty;

import com.bin.netty.netty.vo.MessageResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    private int counter;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            System.out.println("receive msg " + ++counter + " :" + msg + ":start to response");
            //ctx.writeAndFlush(Unpooled.copiedBuffer("read-completed$_".getBytes()));//.addListener(ChannelFutureListener.CLOSE);
            MessageResponse response = new MessageResponse();
            response.setRetCode("0000");
            response.setRetMsg("success:" + msg);
            ctx.write(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("responsed finished! - start  to flush!");
        ctx.flush();
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}

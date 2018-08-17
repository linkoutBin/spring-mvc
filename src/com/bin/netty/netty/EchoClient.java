package com.bin.netty.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.msgpack.MsgpackDecoder;
import io.netty.handler.codec.msgpack.MsgpackEncoder;

public class EchoClient {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 8080;
        new EchoClient().connect(host, port);
    }

    public void connect(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                /*yteBuf del = Unpooled.copiedBuffer("$_".getBytes());
                socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, del));
                socketChannel.pipeline().addLast(new StringDecoder());*/
                /*socketChannel.pipeline().addLast(new MessageEncoder());
                socketChannel.pipeline().addLast(new MessageDecoder());*/
                socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
                socketChannel.pipeline().addLast("msgpack decoder", new MsgpackDecoder());
                socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                socketChannel.pipeline().addLast("msgpack encoder", new MsgpackEncoder());
                socketChannel.pipeline().addLast(new EchoClientHandler());
            }
        });
        try {
            ChannelFuture future = bootstrap.connect(host, port).sync();

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}

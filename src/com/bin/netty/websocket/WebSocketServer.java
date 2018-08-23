package com.bin.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {
    public void run(int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        EventLoopGroup group1 = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group, group1).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast("http-codec", new HttpServerCodec());
                socketChannel.pipeline().addLast("aggregator", new HttpObjectAggregator(65536));
                socketChannel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                socketChannel.pipeline().addLast("handler", new WebSocketServerHandler());
            }
        });
        try {
            Channel ch = bootstrap.bind(port).sync().channel();

            ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
            group1.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new WebSocketServer().run(8080);
    }
}

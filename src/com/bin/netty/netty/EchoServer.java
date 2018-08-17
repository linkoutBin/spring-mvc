package com.bin.netty.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.msgpack.MsgpackDecoder;
import io.netty.handler.codec.msgpack.MsgpackEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {

    public static void main(String[] args) {
        int port = 8080;

        new EchoServer().bind(port);
    }

    public void bind(int port) {
        //配置服务端NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap server = new ServerBootstrap();
        server.group(bossGroup, workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100)
                .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                /*按照指定分割符进行拆包
                ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));*/
                /*按照指定长度进行拆包
                //socketChannel.pipeline().addLast(new FixedLengthFrameDecoder(20));*/
                /*按String类型解码
                socketChannel.pipeline().addLast(new StringDecoder());*/
                /**
                 * 自定义编解码
                 */
                socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65536, 0, 2, 0, 2));
                socketChannel.pipeline().addLast("msgpack decoder", new MsgpackDecoder());
                socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
                socketChannel.pipeline().addLast("msgpack encoder", new MsgpackEncoder());
                //定义具体的请求处理器
                socketChannel.pipeline().addLast(new EchoServerHandler());
            }
        });
        try {
            //绑定端口，同步等待
            ChannelFuture future = server.bind(port).sync();

            //等待监听端口关闭
            future.channel().closeFuture().sync();
        } catch (Exception e) {

        } finally {
            //释放线程池
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}

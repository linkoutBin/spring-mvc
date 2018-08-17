package com.bin.netty.nonblock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class TimeServerNio {
    public static void main(String[] args) {
        /*try (Selector selector = Selector.open()) {
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            for (SelectionKey key : selectionKeys) {
                SocketChannel channel = (SocketChannel) key.channel();
                try {
                    channel.read(byteBuffer);
                } catch (Exception e) {
                }
                byte[] bytes = new byte[byteBuffer.remaining()];
                byteBuffer.get(bytes);
            }
        } catch (IOException ioe) {
        }*/
        System.out.println(new Random().nextInt());
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(args[0]);
        }
        MultiplexerTimeServer multiplexerTimeServer = new MultiplexerTimeServer(port);
        new Thread(multiplexerTimeServer, "Thread-TimeServer-001").start();
    }
}

class MultiplexerTimeServer implements Runnable {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean stop;
    private int queueSizeForWait = 10;

    public MultiplexerTimeServer(int port) {
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress("localhost", port), queueSizeForWait);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("The server is start in port:" + port);
        } catch (Exception e) {
            System.out.println("The Server start failed!");
            e.printStackTrace();
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                SelectionKey key;
                Iterator<SelectionKey> it = selectionKeys.iterator();
                while (it.hasNext()) {
                    key = it.next();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null)
                                key.channel().close();
                        }
                        if (selector.isOpen()) {
                            selector.close();
                        }
                        e.printStackTrace();
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            if (key.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
            if (key.isReadable()) {
                SocketChannel socketChannel = (SocketChannel) key.channel();
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(byteBuffer);
                if (readBytes > 0) {
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    System.out.println("接收请求的内容:" + new String(bytes, Charset.forName("UTF-8")));
                    doResponse(socketChannel);
                } else if (readBytes < 0) {
                    key.cancel();
                    socketChannel.close();
                }
                socketChannel.configureBlocking(false);

            }
        }

    }

    private void doResponse(SocketChannel sc) throws IOException {
        byte[] bytes = "您的请求已经受理".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        sc.write(byteBuffer);
    }
}
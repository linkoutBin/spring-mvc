package com.bin.netty.nonblock;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

public class TimeClientNio {
    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            port = Integer.valueOf(port);
        }
        new Thread(new TimeClentHandle(null, port)).start();
    }
}

class TimeClentHandle implements Runnable {
    private Selector selector;
    private SocketChannel socketChannel;
    private int port;
    private String host;
    private volatile boolean stop;

    public TimeClentHandle(String host, int port) {
        this.port = port;
        this.host = host == null ? "127.0.0.1" : host;
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void run() {
        try {
            doConnect();
            while (!stop) {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey selectionKey;
                while (it.hasNext()) {
                    selectionKey = it.next();
                    it.remove();
                    try {
                        handleInput(selectionKey);
                    } catch (Exception e) {
                        if (selectionKey != null) {
                            selectionKey.cancel();
                            if (selectionKey.channel() != null) {
                                selectionKey.channel().close();
                            }
                        }
                        if (selector.isOpen()) {
                            selector.close();
                        }
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            if (key.isConnectable()) {
                if (socketChannel.finishConnect()) {
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    doWrite(socketChannel);
                } else
                    System.exit(1);
            }
            if (key.isReadable()) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int readBytes = socketChannel.read(byteBuffer);
                if (readBytes > 0) {
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    System.out.println("服务器返回：" + new String(bytes, Charset.forName("UTF-8")));
                    this.stop = true;
                } else if (readBytes < 0) {
                    key.cancel();
                    socketChannel.close();
                }
            }
        }
    }

    private void doConnect() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(host, port))) {
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite(SocketChannel sc) throws IOException {
        byte[] request = "Hello Server!".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(request.length);
        byteBuffer.put(request);
        sc.write(byteBuffer);
        if (!byteBuffer.hasRemaining()) {
            System.out.println("向服务器发送请求成功");
        }
    }
}

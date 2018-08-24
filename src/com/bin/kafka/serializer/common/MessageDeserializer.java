package com.bin.kafka.serializer.common;

import com.bin.kafka.serializer.common.Message;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Map;

public class MessageDeserializer implements Deserializer<Message> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {
        System.out.println("excute configure but do nothing!");
    }

    @Override
    public Message deserialize(String s, byte[] bytes) {
        int length = bytes.length;
        System.out.println("获取到的字节总长度：" + length);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        System.out.println("ByteBuffer:capacity()： " + buffer.capacity() + " position()：" + buffer.position());
        //buffer.flip();
        //System.out.println("ByteBuffer after flip():capacity()： " + buffer.capacity() + " position()：" + buffer.position());
        int messageId = buffer.getInt();
        System.out.println("ByteBuffer now :capacity()： " + buffer.capacity() + " position()：" + buffer.position());
        System.out.println("tostring:" + buffer.toString() + " remain:" + buffer.remaining());
        byte[] original = new byte[length - 4];
        buffer.get(original, 0, length - 4);
        return new Message(messageId, new String(original, Charset.forName("UTF-8")));
    }

    @Override
    public void close() {

    }
}

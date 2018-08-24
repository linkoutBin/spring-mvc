package com.bin.kafka.serializer.common;

import com.bin.kafka.serializer.common.Message;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Map;

public class MessageSerializer implements Serializer<Message> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {
        System.out.println("excute configure-method but do nothing!");
    }

    @Override
    public byte[] serialize(String s, Message message) {
        byte[] content;
        int length;
        if (null == message) return null;
        if (message.getContent() != null) {
            content = message.getContent().getBytes(Charset.forName("UTF-8"));
            length = content.length;
        } else {
            content = new byte[0];
            length = 0;
        }
        System.out.println("序列化总长度：" + (4 + length));
        ByteBuffer buffer = ByteBuffer.allocate(4 + length);
        buffer.putInt(message.getMessageId());
        //buffer.putInt(length);
        buffer.put(content);
        return buffer.array();
    }

    @Override
    public void close() {

    }
}

package com.bin.kafka.serializer.common;

public class Message {
    private int messageId;
    private String content;

    public Message(int messageId, String content) {
        this.messageId = messageId;
        this.content = content;
    }

    public int getMessageId() {
        return messageId;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", content='" + content + '\'' +
                '}';
    }
}

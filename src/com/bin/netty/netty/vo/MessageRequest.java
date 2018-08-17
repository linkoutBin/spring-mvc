package com.bin.netty.netty.vo;

import org.msgpack.annotation.Message;

@Message
public class MessageRequest {
    private String messageId;
    private String message;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageRequest{" +
                "messageId='" + messageId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

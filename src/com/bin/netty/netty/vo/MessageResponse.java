package com.bin.netty.netty.vo;

import org.msgpack.annotation.Message;

@Message
public class MessageResponse {
    private String retCode;
    private String retMsg;

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "retCode='" + retCode + '\'' +
                ", retMsg='" + retMsg + '\'' +
                '}';
    }
}

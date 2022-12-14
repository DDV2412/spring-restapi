package com.ipmugo.library.dto;

import java.util.ArrayList;
import java.util.List;

public class ResponseData<T> {
    public boolean status;

    public List<String> messages = new ArrayList<>();

    public T payload;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

}

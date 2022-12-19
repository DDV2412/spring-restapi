package com.ipmugo.library.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResponseElastic<T> {
    public boolean status;

    public List<String> messages = new ArrayList<>();

    public T payload;

    public List<Map<String, Long>> aggregations;

    public Long totalValue;

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

    public List<Map<String, Long>> getAggregations() {
        return aggregations;
    }

    public void setAggregations(List<Map<String, Long>> aggregations) {
        this.aggregations = aggregations;
    }

    public Long getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Long totalValue) {
        this.totalValue = totalValue;
    }

}

package com.example.tripshopkorea;

import java.util.List;

public class ApiRequest {
    private String model;
    private int max_tokens;
    private List<Message> messages;

    public ApiRequest(String model, int max_tokens, List<Message> messages) {
        this.model = model;
        this.max_tokens = max_tokens;
        this.messages = messages;
    }

}
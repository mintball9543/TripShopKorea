package com.example.tripshopkorea;

import java.util.List;

public class ApiResponse {
    public List<Choice> choices;

    public static class Choice {
        public Message message;
    }

    public static class Message {
        public String role;
        public String content;
    }
}

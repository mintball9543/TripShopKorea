package com.example.tripshopkorea;

public class TranslationResponse {
    public Data data;

    public static class Data {
        public Translation[] translations;

        public static class Translation {
            public String translatedText;
        }
    }
}


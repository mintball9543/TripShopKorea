package com.example.tripshopkorea;


public interface TranslationCallback {
    void onSuccess(String translatedText);
    void onFailure(Throwable t);
}

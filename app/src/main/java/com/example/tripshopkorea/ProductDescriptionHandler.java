package com.example.tripshopkorea;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tripshopkorea.databinding.ActSecondBinding;

public class ProductDescriptionHandler implements ProductDescription.ProductDescriptionCallback {
    private ActSecondBinding binding;
    private Context context;

    public ProductDescriptionHandler(ActSecondBinding binding, Context context) {
        this.binding = binding;
        this.context = context;
    }

    @Override
    public void onSuccess(String description) {
        // SharedPreferences에서 languageCode 가져오기
        SharedPreferences sharedPref = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String languageCode = sharedPref.getString("languageCode", "en"); // Default is English

        // 번역 객체 생성
        Translation translation = new Translation();
        translation.translateText(new TranslationCallback() {
            @Override
            public void onSuccess(String translatedText) {
                // Update the product description with the translated text
                binding.tvDescription.setText(translatedText);
            }

            @Override
            public void onFailure(Throwable t) {
                // Handle failure
            }
        }, description, languageCode);
    }

    @Override
    public void onFailure(Exception e) {
        // Handle the error
    }
}
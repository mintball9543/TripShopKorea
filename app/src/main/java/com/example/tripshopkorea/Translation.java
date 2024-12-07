package com.example.tripshopkorea;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.Response;

public class Translation {

    public static Translation instance;
    TranslateService service;
    private String apiKey = ""; // API 키를 직접 입력

    private Translation(){

        String baseUrl = "https://translation.googleapis.com/";
        Retrofit retrofit = RetrofitClient.getClient(baseUrl);

        service = retrofit.create(TranslateService.class);

    }

    public static Translation getInstance() {
        if (instance == null) {
            synchronized (Translation.class) { // 스레드 안전성
                if (instance == null)
                    instance = new Translation();
            }
        }
        return instance;
    }

    public void translateText(TranslationCallback callback, String text, String languageCode) {
        Call<TranslationResponse> call = service.translate(text, languageCode, apiKey);
        call.enqueue(new Callback<TranslationResponse>() {
            @Override
            public void onResponse(Call<TranslationResponse> call, Response<TranslationResponse> response) {
                if (response.isSuccessful()) {
                    TranslationResponse translationResponse = response.body();
                    if (translationResponse != null) {
                        String translatedText = translationResponse.data.translations[0].translatedText;
                        Log.d("Translation Completed", translatedText);
                        translatedText = translatedText.replace("&gt;", ">");
                        callback.onSuccess(translatedText);

                    }
                } else {
                    Log.e("Translation", "Response not successful: " + response.code());
                    try {
                        Log.e("Translation", "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<TranslationResponse> call, Throwable t) {
                Log.e("Translation", "Error", t);
            }
        });
    }
}

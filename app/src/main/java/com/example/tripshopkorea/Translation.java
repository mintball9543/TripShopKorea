package com.example.tripshopkorea;

import android.util.Log;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.Response;

public class Translation {

    TranslateService service;
    private String apiKey = ""; // API 키를 직접 입력

    public Translation(){

        String baseUrl = "https://translation.googleapis.com/";
        Retrofit retrofit = RetrofitClient.getClient(baseUrl);

        service = retrofit.create(TranslateService.class);

    }

    public void translateText(TranslationCallback callback) {
        Call<TranslationResponse> call = service.translate("안녕하세요", "en", apiKey);
        call.enqueue(new Callback<TranslationResponse>() {
            @Override
            public void onResponse(Call<TranslationResponse> call, Response<TranslationResponse> response) {
                if (response.isSuccessful()) {
                    TranslationResponse translationResponse = response.body();
                    if (translationResponse != null) {
                        String translatedText = translationResponse.data.translations[0].translatedText;
                        Log.d("Translation", translatedText);
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

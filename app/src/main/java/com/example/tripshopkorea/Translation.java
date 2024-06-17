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


    public void getLanguages(TranslationCallback callback) {
        Call<GetLanguages> call = service.getLanguages(apiKey);
        call.enqueue(new Callback<GetLanguages>() {
            @Override
            public void onResponse(Call<GetLanguages> call, Response<GetLanguages> response) {
                if (response.isSuccessful()) {
                    GetLanguages languagesResponse = response.body();
                    if (languagesResponse != null && languagesResponse.data.languages != null) {
                        callback.onSuccess(languagesResponse.data.languages.toString());
                    } else {
                        Log.e("Translation", "Languages response or languages list is null");
                        Log.e("Translation", "Response: " + response.toString()); // Print the entire response
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
            public void onFailure(Call<GetLanguages> call, Throwable t) {
                Log.e("Translation", "Error", t);
            }
        });
    }
}

package com.example.tripshopkorea;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TranslateService {
    @GET("language/translate/v2")
    Call<TranslationResponse> translate(
            @Query("q") String query,
            @Query("target") String targetLanguage,
            @Query("key") String apiKey
    );

    @GET("language/translate/v2/languages")
    Call<GetLanguages> getLanguages(@Query("key") String apiKey);
}
package com.example.tripshopkorea;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenAIService {
    @Headers("Content-Type: application/json")
    @POST("completions")
    Call<ApiResponse> getCompletion(@Body ApiRequest request);
}
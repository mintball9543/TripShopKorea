package com.example.tripshopkorea;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProductDescription {
    private static final String TAG = "ProductDescription err";
    private final OpenAIService service;

    public ProductDescription() {

//        String apiKey = System.getenv("OPENAI_API_KEY"); // API 키를 환경 변수에서 가져오기
        String apiKey = ""; // API 키를 직접 입력
        String baseUrl = "https://api.openai.com/v1/chat/";
        Retrofit retrofit = RetrofitClient.getClient(baseUrl, apiKey);

        service = retrofit.create(OpenAIService.class);
    }

    public void getProductDescription(String productName, String category, final ProductDescriptionCallback callback) throws IOException {

        List<Message> messages = new ArrayList<>();
        messages.add(new Message("user", "상품이름과 카테고리를 통해 상품에 대한 설명을 문장형으로 나열하되, " +
                "만일 상품 이름이 '검색 결과 없음'이면 답변도 '검색 결과 없음' 이라고 할 것. \n 상품 이름 / 카테고리 : "
                + productName + " / " + category));

        ApiRequest request = new ApiRequest("gpt-3.5-turbo", 300, messages);

        Call<ApiResponse> call = service.getCompletion(request);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String description = response.body().choices.get(0).message.content.trim();
                    Log.i(TAG, "상품 설명: " + description);
                    callback.onSuccess(description);
                } else {
                    Log.e(TAG, "API 요청 실패: " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            Log.e(TAG, "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    callback.onFailure(new IOException("API 요청 실패: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "API 요청 중 에러 발생", t);
                callback.onFailure(new IOException("API 요청 중 에러 발생", t));
            }
        });
        /*Response<ApiResponse> response = service.getCompletion(request).execute();

        if (response.isSuccessful() && response.body() != null) {
            String description = response.body().choices.get(0).message.content.trim();
            Log.i(TAG, "상품 설명: " + description);
            return description;
        } else {
            Log.e(TAG, "API 요청 실패: " + response.message());
            if(response.errorBody() != null){
                Log.e(TAG, "Error body: " + response.errorBody().string());
            }
            throw new IOException("API 요청 실패: " + response.message());
        }*/
    }

    public interface ProductDescriptionCallback {
        void onSuccess(String description);
        void onFailure(Exception e);
    }
    /*public void getProductDescription(String productName) {
        ApiRequest request = new ApiRequest("text-davinci-003", "상품 이름: " + productName + "\n상품 설명:", 100);

        service.getCompletion(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String description = response.body().choices.get(0).text.trim();
                    Log.i(TAG, "상품 설명: " + description);
                } else {
                    Log.e(TAG, "API 요청 실패: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "API 요청 중 에러 발생", t);
            }
        });
    }*/
}

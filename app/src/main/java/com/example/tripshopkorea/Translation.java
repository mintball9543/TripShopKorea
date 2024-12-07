package com.example.tripshopkorea;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Translation extends AsyncTask<Void, Void, Void> {
    private String id;
    private String[] name;
    private String[] group;
    private String[] description;
    private String languageCode;
    private MainActivity mainActivity;
    private static Translation instance;
    private TranslateService service;
    private String apiKey = ""; // API key should be set here

    public Translation(MainActivity mainActivity, String id, String[] name, String[] group, String[] description, String languageCode) {
        this.mainActivity = mainActivity;
        this.id = id;
        this.name = name;
        this.group = group;
        this.description = description;
        this.languageCode = languageCode;

        String baseUrl = "https://translation.googleapis.com/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(TranslateService.class);
    }

    public static Translation getInstance(MainActivity mainActivity, String id, String[] name, String[] group, String[] description, String languageCode) {
        if (instance == null) {
            synchronized (Translation.class) { // Thread safety
                if (instance == null)
                    instance = new Translation(mainActivity, id, name, group, description, languageCode);
            }
        }
        return instance;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mainActivity.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final CountDownLatch latch = new CountDownLatch(3);

        // Translate product name
        translateText(new TranslationCallback() {
            @Override
            public void onSuccess(String translatedText) {
                name[0] = translatedText;
                latch.countDown();
            }

            @Override
            public void onFailure(Throwable t) {
                // Handle failure
                latch.countDown();
            }
        }, name[0], languageCode);

        // Translate product group
        translateText(new TranslationCallback() {
            @Override
            public void onSuccess(String translatedText) {
                group[0] = translatedText;
                latch.countDown();
            }

            @Override
            public void onFailure(Throwable t) {
                // Handle failure
                latch.countDown();
            }
        }, group[0], languageCode);

        // Translate description
        translateText(new TranslationCallback() {
            @Override
            public void onSuccess(String translatedText) {
                description[0] = translatedText;
                latch.countDown();
            }

            @Override
            public void onFailure(Throwable t) {
                // Handle failure
                latch.countDown();
            }
        }, description[0], languageCode);

        // Wait for translation to complete
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        // Update DB
        DatabaseHelper db = new DatabaseHelper(mainActivity);
        boolean isUpdated = db.updateData(id, name[0], group[0], description[0]);
        Log.i("DB update", "Updated: " + isUpdated);
        mainActivity.findViewById(R.id.progressBar).setVisibility(View.GONE);
    }

    public void translateText(TranslationCallback callback, String text, String languageCode) {
        Call<TranslationResponse> call = service.translate(text, languageCode, apiKey);
        call.enqueue(new Callback<TranslationResponse>() {
            @Override
            public void onResponse(Call<TranslationResponse> call, Response<TranslationResponse> response) {
                if (response.isSuccessful()) {
                    TranslationResponse translationResponse = response.body();
                    if (translationResponse != null) {
                        String translatedText = translationResponse.data.translationtexts[0].translatedText;
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

    public interface TranslationCallback {
        void onSuccess(String translatedText);
        void onFailure(Throwable t);
    }

    public static class TranslationResponse {
        public Data data;

        public class Data {
            public Translationtext[] translationtexts;

            public class Translationtext {
                public String translatedText;
            }
        }
    }

    public class GetLanguages {
        public Data data;

        public class Data {
            public Language[] languages;

            public class Language {
                public String language;
            }
        }
    }
}
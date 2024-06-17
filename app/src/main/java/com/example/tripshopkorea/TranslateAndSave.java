package com.example.tripshopkorea;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

public class TranslateAndSave extends AsyncTask<Void, Void, Void> {
    private String id;
    private String[] name;
    private String[] group;
    private String[] description;
    private String languageCode;
    private MainActivity mainActivity;
    Translation translation = new Translation();

    public TranslateAndSave(MainActivity mainActivity, String id, String[] name, String[] group, String[] description, String languageCode) {
        this.mainActivity = mainActivity;
        this.id = id;
        this.name = name;
        this.group = group;
        this.description = description;
        this.languageCode = languageCode;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final CountDownLatch latch = new CountDownLatch(3);

        // 상품명 번역
        translation.translateText(new TranslationCallback() {
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

        // 카테고리 번역
        translation.translateText(new TranslationCallback() {
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

        // 설명 번역
        translation.translateText(new TranslationCallback() {
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

        // 번역 완료까지 대기
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

        // DB 업데이트
        DatabaseHelper db = new DatabaseHelper(mainActivity);
        boolean isUpdated = db.updateData(id, name[0], group[0], description[0]);
        Log.i("DB update", "Updated: " + isUpdated);
        mainActivity.loadRecyclerViewData();
    }
}
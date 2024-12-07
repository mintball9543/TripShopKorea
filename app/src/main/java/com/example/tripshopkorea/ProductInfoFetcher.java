package com.example.tripshopkorea;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.bumptech.glide.Glide;
import com.example.tripshopkorea.databinding.ActSecondBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductInfoFetcher {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    public Handler handler = new Handler(Looper.getMainLooper());
    private Context context;

    public ProductInfoFetcher(Context context) {
        this.context = context;
    }

    public void setProductInfo(final String code, ActSecondBinding binding) {
        executorService.execute(() -> {
            try {
                String imgurl = getImageUrl(code);
                String productName = getProductName(code);
                String productGroup = getProductGroup(code);

                // SharedPreferences에서 languageCode 가져오기
                SharedPreferences sharedPref = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                String languageCode = sharedPref.getString("languageCode", "en"); // Default is English

                // 상품명 번역
                Translation translation = Translation.getInstance();
                translation.translateText(new TranslationCallback() {
                    @Override
                    public void onSuccess(String translatedText) {
                        // Update the product name with the translated text
                        handler.post(() -> binding.tvName.setText(translatedText));
                    }

                    @Override
                    public void onFailure(Throwable t) { }
                }, productName, languageCode);

                // 상품 카테고리 번역
                translation.translateText(new TranslationCallback() {
                    @Override
                    public void onSuccess(String translatedText) {
                        // Update the product group with the translated text
                        handler.post(() -> binding.tvGroup.setText(translatedText));
                    }

                    @Override
                    public void onFailure(Throwable t) { }
                }, productGroup, languageCode);

                handler.post(() -> {
                    Glide.with(context)
                            .load(imgurl)
                            .into(binding.imageView);
                    binding.imageurl.setText(imgurl);
                    binding.tvBarcode.setText(code);
                });

                ProductDescription productDescription = ProductDescription.getInstance();
                productDescription.getProductDescription(productName, productGroup, new ProductDescriptionHandler(binding, context));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private String getImageUrl(String code) throws Exception {
        MyScanner myScanner = MyScannerFactory.getScanner(code);
        return myScanner.GetImageUrl(code);
    }

    private String getProductName(String code) throws Exception {
        MyScanner myScanner = MyScannerFactory.getScanner(code);
        return myScanner.GetProductName(code);
    }

    private String getProductGroup(String code) throws Exception {
        MyScanner myScanner = MyScannerFactory.getScanner(code);
        return myScanner.GetProductGroup(code);
    }
}
package com.example.tripshopkorea;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.example.tripshopkorea.databinding.ActSecondBinding;

import org.jsoup.Jsoup;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProductInfoFetcher {
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    public Handler handler = new Handler(Looper.getMainLooper());
    public String resultStr = "";
    private String url = "https://www.koreannet.or.kr/front/koreannet/gtinSrch.do?gtin=";
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
                Translation translation = new Translation();
                translation.translateText(new TranslationCallback() {
                    @Override
                    public void onSuccess(String translatedText) {
                        // Update the product name with the translated text
                        handler.post(() -> binding.tvName.setText(translatedText));
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // Handle failure
                    }
                }, productName, languageCode);

                // 상품 카테고리 번역
                translation.translateText(new TranslationCallback() {
                    @Override
                    public void onSuccess(String translatedText) {
                        // Update the product group with the translated text
                        handler.post(() -> binding.tvGroup.setText(translatedText));
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        // Handle failure
                    }
                }, productGroup, languageCode);

                handler.post(() -> {
                    Glide.with(context)
                            .load(imgurl)
                            .into(binding.imageView);
                    binding.imageurl.setText(imgurl);
                    binding.tvBarcode.setText(code);
                });

                ProductDescription productDescription = new ProductDescription();
                productDescription.getProductDescription(productName, productGroup, new ProductDescriptionHandler(binding, context));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private String getImageUrl(String code) throws Exception {
        String imgurl = Jsoup.connect(url + code).timeout(1000 * 10).get().select("div.img_col img").attr("src");
        if (!imgurl.trim().isEmpty()) {
            return imgurl.trim();
        } else {
            return "검색 결과 없음"; // errMsg
        }
    }

    private String getProductName(String code) throws Exception {
        String productName = Jsoup.connect(url + code).timeout(1000 * 10).get().select("div.nm").text();
        if (!productName.trim().isEmpty()) {
            return productName.trim();
        } else {
            return "검색 결과 없음"; // errMsg
        }
    }

    private String getProductGroup(String code) throws Exception {
        String productGroup = Jsoup.connect(url + code).timeout(1000 * 10).get().select("div.field:contains(KAN 상품분류) div.td").text();

        if (!productGroup.trim().isEmpty()) {
            productGroup = productGroup.substring(productGroup.indexOf(" "));
//            Log.i("productGroup", productGroup.toString());
            return productGroup.trim().toString();
        } else {
            return "검색 결과 없음"; // errMsg
        }
    }
}
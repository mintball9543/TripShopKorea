package com.example.tripshopkorea;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

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

                handler.post(() -> {
                            Glide.with(context)
                                    .load(imgurl)
                                    .into(binding.imageView);
                            binding.imageurl.setText(imgurl);
                            binding.tvBarcode.setText(code);
                            binding.tvName.setText(productName);
                            binding.tvGroup.setText(productGroup);
                });

                ProductDescription productDescription = new ProductDescription();
                productDescription.getProductDescription(productName, productGroup,new ProductDescriptionHandler(binding));

//                secondFragment.updateUI(imgurl, code, productName, productGroup, productDescription);
//                saveProductInfo(code, productName, productGroup, productDescription);
//                resultStr = getResultFromApi(code).toString();
//                Log.i("resultStr", resultStr);
                // UI 업데이트는 UI 스레드에서 실행
//                handler.post(() -> {
//                    // txtProductName.setText(resultStr); // 예시: UI 업데이트
//                });
            } catch (Exception e) {
//                resultStr = e.getMessage();
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
            return productGroup.trim();
        } else {
            return "검색 결과 없음"; // errMsg
        }
    }
}
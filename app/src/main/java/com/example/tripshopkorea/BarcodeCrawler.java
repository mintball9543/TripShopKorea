package com.example.tripshopkorea;

import android.util.Log;

import org.jsoup.Jsoup;

public class BarcodeCrawler implements Crawler {
    private final String url = "https://www.koreannet.or.kr/front/koreannet/gtinSrch.do?gtin=";

    @Override
    public String ProductNameCrawler(String code) throws Exception {
        String productName = Jsoup.connect(url + code).timeout(1000 * 10).get().select("div.nm").text();
        if (!productName.trim().isEmpty()) {
            Log.i("productName", productName.trim());
            return productName.trim();
        } else {
            return "검색 결과 없음"; // errMsg
        }
    }

    @Override
    public String ImageUrlCrawler(String code) throws Exception {
        String imgurl = Jsoup.connect(url + code).timeout(1000 * 10).get().select("div.img_col img").attr("src");
        if (!imgurl.trim().isEmpty()) {
            Log.i("imgurl", imgurl.trim());
            return imgurl.trim();
        } else {
            return "검색 결과 없음"; // errMsg
        }
    }

    @Override
    public String ProductGroupCrawler(String code) throws Exception {
        String productGroup = Jsoup.connect(url + code).timeout(1000 * 10).get().select("div.field:contains(KAN 상품분류) div.td").text();
        if (!productGroup.trim().isEmpty()) {
            productGroup = productGroup.substring(productGroup.indexOf(" "));
            Log.i("productGroup", productGroup);
            return productGroup.trim();
        } else {
            return "검색 결과 없음"; // errMsg
        }
    }
}

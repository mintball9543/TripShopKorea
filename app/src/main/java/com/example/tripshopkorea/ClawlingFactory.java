package com.example.tripshopkorea;

public class ClawlingFactory {
    public static Crawler getClawler(String type){
        if(type.startsWith("https://www.foodqr.kr/"))
            return new QrCrawler();
        else if(type.matches("\\d{13}") || type.matches("\\d{12}"))
            return new BarcodeCrawler();
        else
            return null;
    }
}

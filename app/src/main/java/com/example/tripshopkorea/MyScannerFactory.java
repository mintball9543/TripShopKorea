package com.example.tripshopkorea;

public class MyScannerFactory {
    public static MyScanner getClawler(String type){
        if(type.startsWith("https://www.foodqr.kr/"))
            return new QrMyScanner();
        else if(type.matches("\\d{13}") || type.matches("\\d{12}"))
            return new BarcodeMyScanner();
        else
            return null;
    }
}

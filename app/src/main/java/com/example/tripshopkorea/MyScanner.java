package com.example.tripshopkorea;

public interface MyScanner {

    String GetProductName(String code) throws Exception;
    String GetImageUrl(String code) throws Exception;
    String GetProductGroup(String code) throws Exception;

}
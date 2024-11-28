package com.example.tripshopkorea;

public interface Crawler {

    String ProductNameCrawler(String code) throws Exception;
    String ImageUrlCrawler(String code) throws Exception;
    String ProductGroupCrawler(String code) throws Exception;

}
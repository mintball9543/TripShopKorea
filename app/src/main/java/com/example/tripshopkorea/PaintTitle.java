package com.example.tripshopkorea;

public class PaintTitle {
    String imageURL;
    String barcode;
    String name;
    String group;
    String detail_msg;
    public PaintTitle(String url, String barcode, String name, String group, String detail_msg) {
        this.imageURL = url;
        this.barcode = barcode;
        this.name = name;
        this.group = group;
        this.detail_msg = detail_msg;
    }
}
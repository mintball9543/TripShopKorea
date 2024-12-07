package com.example.tripshopkorea;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QrMyScanner implements MyScanner {
    @Override
    public String GetProductName(String code) throws Exception {
        Document doc = Jsoup.connect(code).timeout(1000 * 10).get();

        // script 태그
        Elements scripts = doc.select("script");
        for (Element script : scripts) {
            String scriptContent = script.html();
            // foodLab JSON 데이터
            Pattern pattern = Pattern.compile("var foodLab = JSON\\.parse\\(jsonEscape\\('(.+?)'\\)\\)\\[0\\];");
            Matcher matcher = pattern.matcher(scriptContent);
            if (matcher.find()) {
                String jsonData = matcher.group(1);
                jsonData = jsonData.replace("\\n", "\\n")
                        .replace("\\'", "\\'")
                        .replace("\\\"", "\\\"")
                        .replace("\\&", "\\&")
                        .replace("\\r", "\\r")
                        .replace("\\t", "\\t")
                        .replace("\\b", "\\b")
                        .replace("\\f", "\\f")
                        .replaceAll("[\\u0000-\\u0019]+", "");

                JSONObject foodLab = new JSONArray(jsonData).getJSONObject(0);
                return foodLab.getString("PRDLST_NM");
            }
        }
        return "검색 결과 없음";
    }

    @Override
    public String GetImageUrl(String code) throws Exception {
        Document doc = Jsoup.connect(code).timeout(1000 * 10).get();

        // script 태그
        Elements scripts = doc.select("script");
        for (Element script : scripts) {
            String scriptContent = script.html();
            // foodLab JSON 데이터
            Pattern pattern = Pattern.compile("var foodLab = JSON\\.parse\\(jsonEscape\\('(.+?)'\\)\\)\\[0\\];");
            Matcher matcher = pattern.matcher(scriptContent);
            if (matcher.find()) {
                String jsonData = matcher.group(1);
                jsonData = jsonData.replace("\\n", "\\n")
                        .replace("\\'", "\\'")
                        .replace("\\\"", "\\\"")
                        .replace("\\&", "\\&")
                        .replace("\\r", "\\r")
                        .replace("\\t", "\\t")
                        .replace("\\b", "\\b")
                        .replace("\\f", "\\f")
                        .replaceAll("[\\u0000-\\u0019]+", "");

                JSONObject foodLab = new JSONArray(jsonData).getJSONObject(0);
                String productImg = foodLab.getString("PRDLST_IMG");
                return "https://www.foodqr.kr/img/prdlst/" + productImg;
            }
        }
        return "검색 결과 없음";
    }

    @Override
    public String GetProductGroup(String code) throws Exception {
        Document doc = Jsoup.connect(code).timeout(1000 * 10).get();

        // script 태그
        Elements scripts = doc.select("script");
        for (Element script : scripts) {
            String scriptContent = script.html();
            // foodLab JSON 데이터
            Pattern pattern = Pattern.compile("var foodLab = JSON\\.parse\\(jsonEscape\\('(.+?)'\\)\\)\\[0\\];");
            Matcher matcher = pattern.matcher(scriptContent);
            if (matcher.find()) {
                String jsonData = matcher.group(1);
                jsonData = jsonData.replace("\\n", "\\n")
                        .replace("\\'", "\\'")
                        .replace("\\\"", "\\\"")
                        .replace("\\&", "\\&")
                        .replace("\\r", "\\r")
                        .replace("\\t", "\\t")
                        .replace("\\b", "\\b")
                        .replace("\\f", "\\f")
                        .replaceAll("[\\u0000-\\u0019]+", "");

                JSONObject foodLab = new JSONArray(jsonData).getJSONObject(0);
                return foodLab.getString("PRDLST_CD_NM");
            }
        }
        return "검색 결과 없음";
    }
}

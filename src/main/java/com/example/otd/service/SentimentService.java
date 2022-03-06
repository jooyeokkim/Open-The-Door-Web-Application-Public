package com.example.otd.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SentimentService {
    private final String HTTP_REQUEST = "https://naveropenapi.apigw.ntruss.com/sentiment-analysis/v1/analyze";

    @Value("${sentiment.client.id}")
    private String clientId;

    @Value("${sentiment.client.secret}")
    private String clientSecret;

    public String getSentimentJsonStr(String content) {

        String jsonResult = "";

        RestTemplate restTemplate = new RestTemplate();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", content);
        String body = jsonObject.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.add("X-NCP-APIGW-API-KEY", clientSecret);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try{
            jsonResult = restTemplate.postForObject(HTTP_REQUEST, request, String.class);
        } catch (Exception e){
            e.printStackTrace();
            return "error";
        }
        return jsonResult;
    }
}

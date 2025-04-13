package com.apinayami.demo.service.Impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.apinayami.demo.dto.request.GHTKRequestDTO;
import com.apinayami.demo.service.IShippingSerivce;


import reactor.core.publisher.Mono;

@Service
public class ShippingServiceImpl implements IShippingSerivce {
    private final WebClient webClient;
    private final String apiKey;

    public ShippingServiceImpl(@Value("${GHTK_KEY}") String apiKey) {
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl("https://services.giaohangtietkiem.vn/services/shipment")
                .defaultHeader("Token", this.apiKey)              
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    // public GHTKServiceImpl(@Value("${GHTK_KEY}") String apiKey) {
    //     this.webClient = WebClient.builder()
    //             .baseUrl("https://services.giaohangtietkiem.vn/services/shipment")
    //             .defaultHeader(HttpHeaders.AUTHORIZATION, apiKey)
    //             .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    //             .build();
    // }
 
    public Mono<String> getShippingFee(GHTKRequestDTO request) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("pick_province", "Đồng Nai");
        requestBody.put("pick_district", "Thành phố Biên Hòa");
        requestBody.put("pick_ward", "Phường Long Bình Tân");
        requestBody.put("pick_address", "175 tổ 7 KP3A");

        requestBody.put("province", request.getProvince());  
        requestBody.put("district", request.getDistrict());  
        requestBody.put("address", request.getAddress());   

        requestBody.put("customer_tel", "0915416485");
        requestBody.put("weight", 500);
        requestBody.put("transport", "road");
        requestBody.put("deliver_option", null);
        requestBody.put("is_xfast", "");
        requestBody.put("is_economy", 0);
        requestBody.put("d_booking", "");
        requestBody.put("package_type", "express");
        requestBody.put("pick_option", "cod");
        requestBody.put("value", 999.999);
        requestBody.put("date_to_delay_pick", "");
        requestBody.put("is_breakable", 0);
        requestBody.put("is_food", 0);
        requestBody.put("tags", new int[]{14});
        requestBody.put("xfast_type", "");
        requestBody.put("delivery_supporters", 0);
        requestBody.put("is_export", 0);

        return webClient.post()
                .uri("/fee")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class);
    }

    
}

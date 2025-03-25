package com.apinayami.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apinayami.demo.dto.request.GHTKRequestDTO;
import com.apinayami.demo.service.IGHTKSerivce;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/ship")
@RequiredArgsConstructor
public class ShippingController {
    private final IGHTKSerivce ghtkSerivce;

    @PostMapping("/fee")
    public Mono<String> getShippingFee(@RequestBody GHTKRequestDTO request) {
        return ghtkSerivce.getShippingFee(request);
    }
}

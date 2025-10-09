package com.apinayami.demo.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apinayami.demo.dto.request.GHTKRequestDTO;
import com.apinayami.demo.service.IShippingSerivce;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/ship")
@RequiredArgsConstructor
public class ShippingController {
    private final IShippingSerivce shippingSerivce;

    @PostMapping("/fee")
    public Mono<String> getShippingFee(@Valid @RequestBody GHTKRequestDTO request) {
        return shippingSerivce.getShippingFee(request);
    }
}

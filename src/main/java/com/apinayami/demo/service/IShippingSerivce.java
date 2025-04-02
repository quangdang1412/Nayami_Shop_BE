package com.apinayami.demo.service;

import com.apinayami.demo.dto.request.GHTKRequestDTO;

import reactor.core.publisher.Mono;

public interface IShippingSerivce {
    Mono<String> getShippingFee(GHTKRequestDTO request);
}

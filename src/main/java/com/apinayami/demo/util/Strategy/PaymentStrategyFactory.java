package com.apinayami.demo.util.Strategy;

import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentStrategyFactory {
    private final Map<String, PaymentStrategy> strategies;
    

    public PaymentStrategy getStrategy(String paymentMethod) {
        return strategies.getOrDefault(paymentMethod, strategies.get("COD"));
    }
}

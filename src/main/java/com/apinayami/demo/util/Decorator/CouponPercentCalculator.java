package com.apinayami.demo.util.Decorator;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CouponPercentCalculator implements IPriceCalculator {
    private IPriceCalculator priceCalculator;
    private double percent;


    @Override
    public double caculatePrice(double totalPrice) {
        return priceCalculator.caculatePrice(totalPrice) - (totalPrice * percent / 100);
    }
    
}

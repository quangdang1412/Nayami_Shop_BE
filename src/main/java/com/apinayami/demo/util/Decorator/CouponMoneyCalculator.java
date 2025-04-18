package com.apinayami.demo.util.Decorator;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CouponMoneyCalculator implements IPriceCalculator {
    private IPriceCalculator priceCalculator;
    private double couponDiscount;


    @Override
    public double caculatePrice(double totalPrice) {
        return priceCalculator.caculatePrice(totalPrice) - couponDiscount;
    }
    
}

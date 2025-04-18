package com.apinayami.demo.util.Decorator;

public class NoCouponCalculator implements IPriceCalculator {
    @Override
    public double caculatePrice(double totalPrice) {
        return totalPrice;
    }
    
}

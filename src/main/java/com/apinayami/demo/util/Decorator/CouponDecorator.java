package com.apinayami.demo.util.Decorator;

import com.apinayami.demo.exception.CustomException;
import com.apinayami.demo.model.BillModel;
import com.apinayami.demo.model.CouponModel;
import com.apinayami.demo.util.Enum.ETypeCoupon;

public class CouponDecorator extends BillDecorator {
    public CouponDecorator(BillModel billModel, CouponModel coupon) {
        super(billModel);
        applyCoupon(coupon);
    }

    private void applyCoupon(CouponModel coupon) {
        if (coupon.getConstraintMoney() != null &&
                billModel.getTotalPrice() < coupon.getConstraintMoney()) {
            throw new CustomException("Đơn hàng không đủ điều kiện sử dụng mã giảm giá này");
        }

        Double totalPrice = billModel.getTotalPrice();
        if (coupon.getValue() != null && coupon.getValue() > 0) {
            if (coupon.getType() == ETypeCoupon.PERCENT) {
                totalPrice -= totalPrice * (coupon.getValue() / 100);
            } else if (coupon.getType() == ETypeCoupon.MONEY) {
                totalPrice -= coupon.getValue();
            }
        }

        billModel.setTotalPrice(totalPrice);
    }

    public BillModel getBillModel() {
        return this.billModel;
    }
}

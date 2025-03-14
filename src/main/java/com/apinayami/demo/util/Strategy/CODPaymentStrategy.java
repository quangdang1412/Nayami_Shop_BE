package com.apinayami.demo.util.Strategy;


import org.springframework.stereotype.Component;
import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.model.PaymentModel;
import com.apinayami.demo.util.Enum.EPaymentCurrency;
import com.apinayami.demo.util.Enum.EPaymentStatus;

@Component("COD")
public class CODPaymentStrategy implements PaymentStrategy {
    @Override
    public PaymentModel processPayment(BillRequestDTO request) {
        return PaymentModel.builder()
                .paymentMethod(request.getPaymentMethod())
                .currency(EPaymentCurrency.VND)
                .paymentStatus(EPaymentStatus.PENDING) 
                .build();
    }
}

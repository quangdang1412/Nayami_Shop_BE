package com.apinayami.demo.util.Strategy;

import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.model.PaymentModel;

public interface PaymentStrategy {
    PaymentModel processPayment(BillRequestDTO request);
}
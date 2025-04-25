package com.apinayami.demo.util.Strategy;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.model.PaymentModel;
import com.apinayami.demo.util.Enum.EPaymentCurrency;
import com.apinayami.demo.util.Enum.EPaymentStatus;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;

@Component("ONLINE_BANKING")
@Slf4j
public class OnlineBankingPaymentStrategy implements PaymentStrategy {

    @Value("${payos.client-id}")
    private String clientId;

    @Value("${payos.api-key}")
    private String apiKey;

    @Value("${payos.checksum-key}")
    private String checksumKey;

    @Override
    public PaymentModel processPayment(BillRequestDTO request) {
        return PaymentModel.builder()
                .paymentMethod(request.getPaymentMethod())
                .currency(EPaymentCurrency.VND)
                .paymentStatus(EPaymentStatus.PENDING)
                .build();
    }

    public String createCheckout(Integer finalTotal, Long orderId, String returnUrl, String cancelUrl) {
        try {
            if (finalTotal == null || finalTotal <= 0) {
                throw new IllegalArgumentException("Số tiền thanh toán không hợp lệ!");
            }
            if (orderId == null ) {
                throw new IllegalArgumentException("Mã đơn hàng không hợp lệ!");
            }

            PayOS payos = new PayOS(clientId, apiKey, checksumKey);
            SecureRandom random = new SecureRandom();
            long billId = Math.abs(random.nextLong());
            billId = billId % 100_000_000L;

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(billId)
                    .description("Đơn hàng: " + orderId)
                    .amount(finalTotal)
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .build();

            CheckoutResponseData data = payos.createPaymentLink(paymentData);
            return data.getCheckoutUrl();
        } catch (Exception e) {
            log.error("Lỗi khi tạo link thanh toán: {}", e.getMessage());
            return "Lỗi: " + e.getMessage();
        }
    }
}
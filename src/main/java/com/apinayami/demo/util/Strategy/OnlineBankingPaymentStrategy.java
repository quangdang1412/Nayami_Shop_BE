package com.apinayami.demo.util.Strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.apinayami.demo.dto.request.BillRequestDTO;
import com.apinayami.demo.model.PaymentModel;
import com.apinayami.demo.util.Enum.EPaymentCurrency;
import com.apinayami.demo.util.Enum.EPaymentStatus;
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

    public String createCheckout(Integer finalTotal, String orderId, String returnUrl, String cancelUrl) {
        try {
            if (finalTotal == null || finalTotal <= 0) {
                throw new IllegalArgumentException("Số tiền thanh toán không hợp lệ!");
            }
            if (orderId == null || orderId.isBlank()) {
                throw new IllegalArgumentException("Mã đơn hàng không hợp lệ!");
            }

            PayOS payos = new PayOS(clientId, apiKey, checksumKey);
            Long orderCode = System.currentTimeMillis() / 1000;

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
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
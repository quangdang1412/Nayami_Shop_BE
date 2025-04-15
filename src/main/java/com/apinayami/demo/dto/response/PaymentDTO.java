package com.apinayami.demo.dto.response;

import com.apinayami.demo.util.Enum.EPaymentCurrency;
import com.apinayami.demo.util.Enum.EPaymentMethod;
import com.apinayami.demo.util.Enum.EPaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDTO implements Serializable {
    private EPaymentStatus paymentStatus;
    private EPaymentCurrency currency;
    private EPaymentMethod paymentMethod;
}

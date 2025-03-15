package com.apinayami.demo.model;

import com.apinayami.demo.util.Enum.EPaymentCurrency;
import com.apinayami.demo.util.Enum.EPaymentMethod;
import com.apinayami.demo.util.Enum.EPaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentModel extends AbstractEntity<Long> {
    @Enumerated(EnumType.STRING)
    private EPaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
    private EPaymentCurrency currency;
    @Enumerated(EnumType.STRING)
    private EPaymentMethod paymentMethod;
}

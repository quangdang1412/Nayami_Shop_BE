package com.apinayami.demo.model;

import com.apinayami.demo.util.Enum.EPaymentCurrency;
import com.apinayami.demo.util.Enum.EPaymentMethod;
import com.apinayami.demo.util.Enum.EPaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PaymentModel extends AbstractEntity<Long> {
    private EPaymentStatus paymentStatus;
    private EPaymentCurrency currency;
    private EPaymentMethod paymentMethod;
    @OneToOne
    @JoinColumn(name = "bill_id")
    private BillModel billModel;
}

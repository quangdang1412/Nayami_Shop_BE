package com.apinayami.demo.repository;

import com.apinayami.demo.model.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPaymentRepository extends JpaRepository<PaymentModel, Long> {
}

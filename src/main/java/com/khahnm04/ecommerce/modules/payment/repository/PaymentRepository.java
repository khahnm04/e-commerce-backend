package com.khahnm04.ecommerce.modules.payment.repository;

import com.khahnm04.ecommerce.modules.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderIdAndDeletedAtIsNull(Long orderId);

}

package Ecommerce.Application.project.modules.payment.repository;

import Ecommerce.Application.project.modules.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    List<Payment> findByOrderUserEmail(String email);

    Optional<Payment> findTopByOrderUserEmailOrderByCreatedAtDesc(String email);
}

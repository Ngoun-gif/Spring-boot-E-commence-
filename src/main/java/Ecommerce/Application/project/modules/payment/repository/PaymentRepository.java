package Ecommerce.Application.project.modules.payment.repository;

import Ecommerce.Application.project.modules.payment.entity.Payment;
import Ecommerce.Application.project.modules.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder(Order order);
}

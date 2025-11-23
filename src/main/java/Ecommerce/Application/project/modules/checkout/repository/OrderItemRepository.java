package Ecommerce.Application.project.modules.checkout.repository;

import Ecommerce.Application.project.modules.checkout.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

package Ecommerce.Application.project.modules.order.repository;

import Ecommerce.Application.project.modules.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}

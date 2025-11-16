package Ecommerce.Application.project.modules.order.repository;

import Ecommerce.Application.project.modules.order.entity.Order;
import Ecommerce.Application.project.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}

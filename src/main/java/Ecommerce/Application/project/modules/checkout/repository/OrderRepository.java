package Ecommerce.Application.project.modules.checkout.repository;

import Ecommerce.Application.project.modules.checkout.entity.Order;
import Ecommerce.Application.project.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}

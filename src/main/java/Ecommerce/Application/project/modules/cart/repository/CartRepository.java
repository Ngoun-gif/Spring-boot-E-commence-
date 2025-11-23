package Ecommerce.Application.project.modules.cart.repository;

import Ecommerce.Application.project.modules.cart.entity.Cart;
import Ecommerce.Application.project.modules.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);

    Optional<Cart> findByUserId(Long userId);
}

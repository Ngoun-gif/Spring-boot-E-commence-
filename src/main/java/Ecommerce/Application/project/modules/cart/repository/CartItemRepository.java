package Ecommerce.Application.project.modules.cart.repository;

import Ecommerce.Application.project.modules.cart.entity.CartItem;
import Ecommerce.Application.project.modules.cart.entity.Cart;
import Ecommerce.Application.project.modules.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}

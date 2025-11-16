package Ecommerce.Application.project.modules.wishlist.repository;

import Ecommerce.Application.project.modules.wishlist.entity.Wishlist;
import Ecommerce.Application.project.modules.users.entity.User;
import Ecommerce.Application.project.modules.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUser(User user);

    Optional<Wishlist> findByUserAndProduct(User user, Product product);
}

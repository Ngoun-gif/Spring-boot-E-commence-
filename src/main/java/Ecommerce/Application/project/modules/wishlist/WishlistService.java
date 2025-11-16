package Ecommerce.Application.project.modules.wishlist;

import Ecommerce.Application.project.modules.wishlist.dto.WishlistResponse;
import Ecommerce.Application.project.modules.wishlist.entity.Wishlist;
import Ecommerce.Application.project.modules.wishlist.repository.WishlistRepository;
import Ecommerce.Application.project.modules.products.ProductRepository;
import Ecommerce.Application.project.modules.users.UserRepository;
import Ecommerce.Application.project.modules.users.entity.User;
import Ecommerce.Application.project.modules.products.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // Add product to wishlist
    public String addToWishlist(String userEmail, Long productId) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // prevent duplicate
        wishlistRepository.findByUserAndProduct(user, product)
                .ifPresent(w -> {
                    throw new RuntimeException("Already in wishlist");
                });

        Wishlist w = Wishlist.builder()
                .user(user)
                .product(product)
                .build();

        wishlistRepository.save(w);
        return "Added to wishlist";
    }

    // Get user wishlist
    public List<WishlistResponse> getWishlist(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return wishlistRepository.findByUser(user).stream()
                .map(w -> WishlistResponse.builder()
                        .productId(w.getProduct().getId())
                        .title(w.getProduct().getTitle())
                        .image(w.getProduct().getImage())
                        .price(w.getProduct().getPrice().doubleValue())
                        .build()
                ).toList();
    }

    // Remove product
    public String remove(String userEmail, Long productId) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist item = wishlistRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new RuntimeException("Item not in wishlist"));

        wishlistRepository.delete(item);

        return "Removed";
    }
}

package Ecommerce.Application.project.modules.cart;

import Ecommerce.Application.project.modules.cart.dto.*;
import Ecommerce.Application.project.modules.cart.entity.*;
import Ecommerce.Application.project.modules.cart.repository.*;
import Ecommerce.Application.project.modules.products.ProductRepository;
import Ecommerce.Application.project.modules.users.UserRepository;
import Ecommerce.Application.project.modules.users.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    // -----------------------------------------------------
    // FIND or CREATE cart
    // -----------------------------------------------------
    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>());
            cart.setTotalPrice(BigDecimal.ZERO);
            return cartRepository.save(cart);
        });
    }

    // -----------------------------------------------------
    // ADD ITEM TO CART
    // -----------------------------------------------------
    public CartResponse addToCart(String userEmail, AddToCartRequest request) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        var product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart = getOrCreateCart(user);

        // Check if product already exists in cart
        CartItem existingItem = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            existingItem.setTotal(existingItem.getPrice()
                    .multiply(BigDecimal.valueOf(existingItem.getQuantity())));
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            newItem.setPrice(product.getPrice());
            newItem.setTotal(product.getPrice()
                    .multiply(BigDecimal.valueOf(request.getQuantity())));

            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        updateCartTotal(cart);

        return toResponse(cart);
    }

    // -----------------------------------------------------
    // UPDATE ITEM QUANTITY
    // -----------------------------------------------------
    public CartResponse updateItem(String userEmail, UpdateCartRequest request) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = getOrCreateCart(user);

        CartItem item = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Item does not belong to this cart");
        }

        item.setQuantity(request.getQuantity());
        item.setTotal(item.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
        cartItemRepository.save(item);

        updateCartTotal(cart);

        return toResponse(cart);
    }

    // -----------------------------------------------------
    // REMOVE ITEM
    // -----------------------------------------------------
    public CartResponse removeItem(String userEmail, Long itemId) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = getOrCreateCart(user);

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Item does not belong to your cart");
        }

        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        updateCartTotal(cart);

        return toResponse(cart);
    }

    // -----------------------------------------------------
    // CLEAR CART
    // -----------------------------------------------------
    public CartResponse clearCart(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = getOrCreateCart(user);

        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();

        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        return toResponse(cart);
    }

    // -----------------------------------------------------
    // GET CART
    // -----------------------------------------------------
    public CartResponse getCart(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = getOrCreateCart(user);

        return toResponse(cart);
    }

    // -----------------------------------------------------
    // Recalculate total
    // -----------------------------------------------------
    private void updateCartTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(CartItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(total);
        cartRepository.save(cart);
    }

    // -----------------------------------------------------
    // Convert to DTO
    // -----------------------------------------------------
    private CartResponse toResponse(Cart cart) {
        return CartResponse.builder()
                .cartId(cart.getId())
                .totalPrice(cart.getTotalPrice())
                .items(
                        cart.getItems().stream()
                                .map(item -> CartItemResponse.builder()
                                        .id(item.getId())
                                        .productId(item.getProduct().getId())
                                        .quantity(item.getQuantity())
                                        .price(item.getPrice())
                                        .total(item.getTotal())
                                        .build()
                                ).toList()
                )
                .build();
    }

}

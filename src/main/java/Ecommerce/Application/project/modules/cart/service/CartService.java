package Ecommerce.Application.project.modules.cart.service;

import Ecommerce.Application.project.modules.cart.dto.*;
import Ecommerce.Application.project.modules.cart.entity.*;
import Ecommerce.Application.project.modules.cart.repository.*;
import Ecommerce.Application.project.modules.products.entity.Product;
import Ecommerce.Application.project.modules.products.ProductRepository;
import Ecommerce.Application.project.modules.stock.entity.Stock;
import Ecommerce.Application.project.modules.stock.repository.StockRepository;
import Ecommerce.Application.project.modules.users.entity.User;
import Ecommerce.Application.project.modules.users.UserRepository;
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
    private final StockRepository stockRepository;

    // -----------------------------------------------------
    // GET OR CREATE CART
    // -----------------------------------------------------
    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = Cart.builder()
                    .user(user)
                    .totalPrice(BigDecimal.ZERO)
                    .items(new ArrayList<>())
                    .build();
            return cartRepository.save(cart);
        });
    }

    // -----------------------------------------------------
    // ADD TO CART
    // -----------------------------------------------------
    public CartResponse addToCart(String userEmail, AddToCartRequest request) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Stock stock = stockRepository.findByProduct(product)
                .orElseThrow(() -> new RuntimeException("Stock record missing"));

        int qty = request.getQuantity();

        if (qty <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        if (stock.getQuantity() < qty) {
            throw new RuntimeException("Only " + stock.getQuantity() + " available");
        }

        CartItem item = cartItemRepository.findByCartAndProduct(cart, product).orElse(null);

        if (item == null) {
            item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .price(product.getPrice())
                    .quantity(qty)
                    .total(product.getPrice().multiply(BigDecimal.valueOf(qty)))
                    .outOfStock(stock.getQuantity() < qty)
                    .availableStock(stock.getQuantity())
                    .build();

            cart.getItems().add(item);

        } else {
            int newQty = item.getQuantity() + qty;

            if (newQty > stock.getQuantity()) {
                throw new RuntimeException("Not enough stock");
            }

            item.setQuantity(newQty);
            item.setTotal(item.getPrice().multiply(BigDecimal.valueOf(newQty)));
            item.setOutOfStock(stock.getQuantity() < newQty);
            item.setAvailableStock(stock.getQuantity());
        }

        cartItemRepository.save(item);
        updateCartTotal(cart);

        return toResponse(cart);
    }

    // -----------------------------------------------------
    // UPDATE ITEM
    // -----------------------------------------------------
    public CartResponse updateItem(String userEmail, UpdateCartRequest request) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = getOrCreateCart(user);

        CartItem item = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Item does not belong to your cart");
        }

        int newQty = request.getQuantity();

        if (newQty <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        Stock stock = stockRepository.findByProduct(item.getProduct())
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        if (newQty > stock.getQuantity()) {
            throw new RuntimeException("Only " + stock.getQuantity() + " available");
        }

        item.setQuantity(newQty);
        item.setTotal(item.getPrice().multiply(BigDecimal.valueOf(newQty)));
        item.setOutOfStock(stock.getQuantity() < newQty);
        item.setAvailableStock(stock.getQuantity());

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
            throw new RuntimeException("Item does not belong to this cart");
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
    // UPDATE TOTAL PRICE
    // -----------------------------------------------------
    private void updateCartTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(CartItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(total);
        cartRepository.save(cart);
    }

    // -----------------------------------------------------
    // CONVERT TO DTO
    // -----------------------------------------------------
    private CartResponse toResponse(Cart cart) {
        return CartResponse.builder()
                .cartId(cart.getId())
                .totalPrice(cart.getTotalPrice())
                .items(
                        cart.getItems().stream().map(item -> {

                            Stock stock = stockRepository.findByProduct(item.getProduct()).orElse(null);
                            int available = stock != null ? stock.getQuantity() : 0;
                            boolean isOut = available < item.getQuantity();

                            return CartItemResponse.builder()
                                    .id(item.getId())
                                    .productId(item.getProduct().getId())
                                    .quantity(item.getQuantity())
                                    .price(item.getPrice())
                                    .total(item.getTotal())
                                    .outOfStock(isOut)
                                    .availableStock(available)
                                    .build();

                        }).toList()
                )
                .build();
    }
}

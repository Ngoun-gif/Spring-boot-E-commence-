package Ecommerce.Application.project.modules.cart.impl;

import Ecommerce.Application.project.modules.cart.dto.*;
import Ecommerce.Application.project.modules.cart.dto.cart_item.AddToCartRequest;
import Ecommerce.Application.project.modules.cart.dto.cart_item.UpdateCartRequest;
import Ecommerce.Application.project.modules.cart.entity.*;
import Ecommerce.Application.project.modules.cart.repository.*;
import Ecommerce.Application.project.modules.products.entity.Product;
import Ecommerce.Application.project.modules.products.ProductRepository;
import Ecommerce.Application.project.modules.stock.entity.Stock;
import Ecommerce.Application.project.modules.stock.repository.StockRepository;
import Ecommerce.Application.project.modules.users.entity.User;
import Ecommerce.Application.project.modules.users.UserRepository;
import Ecommerce.Application.project.modules.cart.service.CartService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    // -------------------------------------------
    // Helper: Load User
    // -------------------------------------------
    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // -------------------------------------------
    // Helper: Load or create Cart
    // -------------------------------------------
    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .user(user)
                                .totalPrice(BigDecimal.ZERO)
                                .items(new ArrayList<>())
                                .build()
                ));
    }

    // -------------------------------------------
    // Helper: Recalculate total
    // -------------------------------------------
    private void updateTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(CartItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalPrice(total);
        cartRepository.save(cart);
    }

    // -------------------------------------------
    // Helper: Stock check
    // -------------------------------------------
    private int getStock(Product product) {
        return stockRepository.findByProduct(product)
                .map(Stock::getQuantity)
                .orElse(0);
    }

    // -------------------------------------------
    // Helper: Validate cart ownership
    // -------------------------------------------
    private void validateCartOwnership(Cart cart, String userEmail) {
        if (!cart.getUser().getEmail().equals(userEmail)) {
            throw new RuntimeException("Access denied: You don't own this cart");
        }
    }

    // -------------------------------------------
    // ADD TO CART
    // -------------------------------------------
    @Override
    public CartResponse addToCart(String email, AddToCartRequest request) {

        User user = findUser(email);
        Cart cart = getOrCreateCart(user);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int qty = request.getQuantity();
        if (qty <= 0)
            throw new RuntimeException("Quantity must be greater than 0");

        int available = getStock(product);
        if (available < qty)
            throw new RuntimeException("Only " + available + " available");

        CartItem existing = cartItemRepository.findByCartAndProduct(cart, product).orElse(null);

        if (existing == null) {
            CartItem item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .price(product.getPrice())
                    .quantity(qty)
                    .total(product.getPrice().multiply(BigDecimal.valueOf(qty)))
                    .build();

            cart.getItems().add(item);
            cartItemRepository.save(item);

        } else {
            int newQty = existing.getQuantity() + qty;

            if (newQty > available)
                throw new RuntimeException("Not enough stock");

            existing.setQuantity(newQty);
            existing.setTotal(existing.getPrice().multiply(BigDecimal.valueOf(newQty)));

            cartItemRepository.save(existing);
        }

        updateTotal(cart);
        return toResponse(cart);
    }

    // -------------------------------------------
    // UPDATE ITEM
    // -------------------------------------------
    @Override
    public CartResponse updateItem(String email, UpdateCartRequest request) {

        User user = findUser(email);
        Cart cart = getOrCreateCart(user);

        CartItem item = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getCart().getId().equals(cart.getId()))
            throw new RuntimeException("Item does not belong to your cart");

        validateCartOwnership(cart, email);

        int newQty = request.getQuantity();
        if (newQty <= 0)
            throw new RuntimeException("Quantity must be greater than 0");

        int available = getStock(item.getProduct());
        if (newQty > available)
            throw new RuntimeException("Only " + available + " available");

        item.setQuantity(newQty);
        item.setTotal(item.getPrice().multiply(BigDecimal.valueOf(newQty)));

        cartItemRepository.save(item);
        updateTotal(cart);

        return toResponse(cart);
    }

    // -------------------------------------------
    // REMOVE ITEM
    // -------------------------------------------
    @Override
    public CartResponse removeItem(String email, Long itemId) {

        User user = findUser(email);
        Cart cart = getOrCreateCart(user);

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getCart().getId().equals(cart.getId()))
            throw new RuntimeException("Item does not belong to cart");

        validateCartOwnership(cart, email);

        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        updateTotal(cart);

        return toResponse(cart);
    }

    // -------------------------------------------
    // CLEAR CART
    // -------------------------------------------
    @Override
    public CartResponse clearCart(String email) {

        User user = findUser(email);
        Cart cart = getOrCreateCart(user);

        validateCartOwnership(cart, email);

        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        return toResponse(cart);
    }

    // -------------------------------------------
    // GET CART
    // -------------------------------------------
    @Override
    public CartResponse getCart(String email) {

        User user = findUser(email);
        Cart cart = getOrCreateCart(user);

        validateCartOwnership(cart, email);

        return toResponse(cart);
    }

    // -------------------------------------------
    // Convert Cart → CartResponse
    // -------------------------------------------
    private CartResponse toResponse(Cart cart) {
        return CartResponse.builder()
                .cartId(cart.getId())
                .totalPrice(cart.getTotalPrice())
                .items(
                        cart.getItems().stream().map(item -> {

                            int available = getStock(item.getProduct());
                            boolean outOfStock = available < item.getQuantity();

                            return CartItemResponse.builder()
                                    .id(item.getId())
                                    .productId(item.getProduct().getId())
                                    .quantity(item.getQuantity())
                                    .price(item.getPrice())
                                    .total(item.getTotal())
                                    .outOfStock(outOfStock)
                                    .availableStock(available)
                                    .build();

                        }).collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public CartResponse increaseItem(String email, Long itemId) {

        User user = findUser(email);
        Cart cart = getOrCreateCart(user);

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Item does not belong to your cart");
        }

        validateCartOwnership(cart, email);

        int newQty = item.getQuantity() + 1;
        int stock = getStock(item.getProduct());

        if (newQty > stock) {
            throw new RuntimeException("Only " + stock + " available");
        }

        item.setQuantity(newQty);
        item.setTotal(item.getPrice().multiply(BigDecimal.valueOf(newQty)));
        cartItemRepository.save(item);

        updateTotal(cart);
        return toResponse(cart);
    }

    @Override
    public CartResponse decreaseItem(String email, Long itemId) {

        User user = findUser(email);
        Cart cart = getOrCreateCart(user);

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Item does not belong to your cart");
        }

        validateCartOwnership(cart, email);

        int newQty = item.getQuantity() - 1;

        // If quantity becomes 0 → remove item
        if (newQty <= 0) {
            cart.getItems().remove(item);
            cartItemRepository.delete(item);
            updateTotal(cart);
            return toResponse(cart);
        }

        item.setQuantity(newQty);
        item.setTotal(item.getPrice().multiply(BigDecimal.valueOf(newQty)));
        cartItemRepository.save(item);

        updateTotal(cart);
        return toResponse(cart);
    }
}
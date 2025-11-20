package Ecommerce.Application.project.modules.order;

import Ecommerce.Application.project.modules.cart.entity.Cart;
import Ecommerce.Application.project.modules.cart.entity.CartItem;
import Ecommerce.Application.project.modules.cart.repository.CartRepository;
import Ecommerce.Application.project.modules.order.dto.*;
import Ecommerce.Application.project.modules.order.entity.*;
import Ecommerce.Application.project.modules.order.repository.OrderItemRepository;
import Ecommerce.Application.project.modules.order.repository.OrderRepository;
import Ecommerce.Application.project.modules.stock.repository.StockRepository;
import Ecommerce.Application.project.modules.stock.entity.Stock;
import Ecommerce.Application.project.modules.users.UserRepository;
import Ecommerce.Application.project.modules.users.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final StockRepository stockRepository;

    // =====================================
    // CHECKOUT
    // =====================================
    public OrderResponse checkout(String userEmail, CheckoutRequest req) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Your cart is empty");
        }

        // Create new order
        Order order = new Order();
        order.setUser(user);
        order.setPaymentMethod(req.getPaymentMethod());
        order.setStatus("PENDING");
        order.setTotalPrice(cart.getTotalPrice());
        order.setItems(new ArrayList<>()); // FIX: IMPORTANT
        orderRepository.save(order);

        // Convert cart -> order items
        for (CartItem ci : cart.getItems()) {

            Stock stock = stockRepository.findByProduct(ci.getProduct())
                    .orElseThrow(() -> new RuntimeException("Stock not found"));

            if (stock.getQuantity() < ci.getQuantity()) {
                throw new RuntimeException(
                        ci.getProduct().getTitle() + " not enough in stock"
                );
            }

            // Deduct stock
            stock.setQuantity(stock.getQuantity() - ci.getQuantity());
            stockRepository.save(stock);

            // Create order item
            OrderItem item = OrderItem.builder()
                    .order(order)
                    .product(ci.getProduct())
                    .quantity(ci.getQuantity())
                    .price(ci.getPrice())
                    .total(ci.getTotal())
                    .build();

            orderItemRepository.save(item);
            order.getItems().add(item);
        }

        // CLEAR CART
        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        return toResponse(order);
    }

    // =====================================
    // GET ALL ORDERS FOR USER
    // =====================================
    public java.util.List<OrderResponse> getMyOrders(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // =====================================
    // TO RESPONSE
    // =====================================
    private OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .totalPrice(order.getTotalPrice())
                .items(
                        order.getItems().stream()
                                .map(oi -> OrderItemResponse.builder()
                                        .productId(oi.getProduct().getId())
                                        .title(oi.getProduct().getTitle())
                                        .quantity(oi.getQuantity())
                                        .price(oi.getPrice())
                                        .total(oi.getTotal())
                                        .build()
                                ).collect(Collectors.toList())
                )
                .build();
    }
}

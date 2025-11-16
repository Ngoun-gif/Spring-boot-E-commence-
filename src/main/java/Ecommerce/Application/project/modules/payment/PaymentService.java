package Ecommerce.Application.project.modules.payment;

import Ecommerce.Application.project.modules.notification.telegram.TelegramService;
import Ecommerce.Application.project.modules.order.entity.Order;
import Ecommerce.Application.project.modules.order.repository.OrderRepository;
import Ecommerce.Application.project.modules.payment.dto.PaymentRequest;
import Ecommerce.Application.project.modules.payment.dto.PaymentResponse;
import Ecommerce.Application.project.modules.payment.entity.Payment;
import Ecommerce.Application.project.modules.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    private final TelegramService telegramService;   // ✅ FIXED

    public PaymentResponse pay(PaymentRequest req, String email) {

        Order order = orderRepository.findById(req.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("This order does not belong to you");
        }

        // Prevent duplicate payment
        paymentRepository.findByOrder(order)
                .ifPresent(p -> {
                    throw new RuntimeException("Order already paid");
                });

        // Create payment
        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalPrice())
                .method(req.getMethod())
                .status("PAID")
                .transactionId(UUID.randomUUID().toString())
                .build();

        paymentRepository.save(payment);

        // Update order
        order.setStatus("PROCESSING");
        orderRepository.save(order);

        // =====================================
        // SEND TELEGRAM NOTIFICATION (Pretty)
        // =====================================
        String msg = """
                📢 *New Payment Received!*

                👤 *User:* `%s`
                🧾 *Order ID:* `%d`
                💰 *Amount:* *$%s*
                💳 *Method:* `%s`
                🔑 *Transaction:* `%s`
                """
                .formatted(
                        order.getUser().getEmail(),
                        order.getId(),
                        payment.getAmount().toPlainString(),
                        payment.getMethod(),
                        payment.getTransactionId()
                );

        telegramService.sendMessage(msg);

        // Return response
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(order.getId())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .transactionId(payment.getTransactionId())
                .build();
    }
}

package Ecommerce.Application.project.modules.payment;

import Ecommerce.Application.project.modules.checkout.entity.Order;
import Ecommerce.Application.project.modules.checkout.repository.OrderRepository;
import Ecommerce.Application.project.modules.payment.dto.PaymentRequest;
import Ecommerce.Application.project.modules.payment.dto.PaymentResponse;
import Ecommerce.Application.project.modules.payment.entity.Payment;
import Ecommerce.Application.project.modules.payment.enums.PaymentStatus;
import Ecommerce.Application.project.modules.payment.repository.PaymentRepository;
import Ecommerce.Application.project.modules.notification.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final TelegramService telegramService;

    @Transactional
    public PaymentResponse pay(PaymentRequest req, String email) {

        // 1. Find order
        Order order = orderRepository.findById(req.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 2. Security: user must own order
        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("You cannot pay for another user's order");
        }

        // 3. Prevent duplicate payments
        paymentRepository.findByOrderId(order.getId())
                .ifPresent(p -> {
                    throw new RuntimeException("Order already paid");
                });

        // 4. Create payment (NO BUILDER!)
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setTransactionId(UUID.randomUUID().toString());

        paymentRepository.save(payment);

        // 5. Update order payment status
        order.setPaymentStatus(PaymentStatus.PAID);
        orderRepository.save(order);

        // 6. Telegram HTML notification
        String msg = """
                <b>💰 Payment Received!</b>

                👤 <b>User:</b> <code>%s</code>
                📦 <b>Order ID:</b> <code>%d</code>
                💳 <b>Method:</b> <code>%s</code>
                💵 <b>Total Amount:</b> <code>%s</code>

                🔐 <b>Transaction:</b>
                <code>%s</code>

                🛠️ Backend: <b>Ecommerce System</b>
                """.formatted(
                order.getUser().getEmail(),
                order.getId(),
                order.getPaymentMethod(),
                order.getTotalPrice().toPlainString(),
                payment.getTransactionId()
        );

        telegramService.sendHtmlMessage(msg);

        // 7. Return Payment response
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(order.getId())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .createdAt(payment.getCreatedAt())  // ADD THIS!
                .build();
    }
}

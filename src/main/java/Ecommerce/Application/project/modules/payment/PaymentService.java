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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final TelegramService telegramService;

    // ==========================================
    // 1. CREATE PAYMENT
    // ==========================================
    @Transactional
    public PaymentResponse pay(PaymentRequest req, String email) {

        // 🔐 find order
        Order order = orderRepository.findById(req.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 🔐 user must own the order
        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("This order does not belong to the current user");
        }

        // stop double payment
        paymentRepository.findByOrderId(order.getId())
                .ifPresent(p -> {
                    throw new RuntimeException("Order already paid");
                });

        // create payment
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setTransactionId(UUID.randomUUID().toString());
        paymentRepository.save(payment);

        // update order
        order.setPaymentStatus(PaymentStatus.PAID);
        orderRepository.save(order);

        // notify telegram
        sendTelegramPayment(order, payment);

        // return DTO
        return toResponse(payment);
    }

    // ==========================================
    // 2. GET LAST PAYMENT
    // ==========================================
    public PaymentResponse getMyLatestPayment(String email) {
        Payment payment = paymentRepository
                .findTopByOrderUserEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new RuntimeException("No payments found"));

        return toResponse(payment);
    }

    // ==========================================
    // 3. GET ALL PAYMENTS
    // ==========================================
    public List<PaymentResponse> getMyPayments(String email) {
        return paymentRepository.findByOrderUserEmail(email)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ==========================================
    // 4. GET PAYMENT BY ORDER
    // ==========================================
    public PaymentResponse getPaymentByOrder(Long orderId, String email) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // user must own the order
        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized");
        }

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return toResponse(payment);
    }

    // ==========================================
    // 5. MAP ENTITY → DTO
    // ==========================================
    private PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrder().getId())
                .paymentStatus(payment.getPaymentStatus())
                .totalPrice(payment.getOrder().getTotalPrice())  // ✅ FIX
                .transactionId(payment.getTransactionId())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    // ==========================================
    // TELEGRAM MESSAGE
    // ==========================================
    private void sendTelegramPayment(Order order, Payment payment) {

        String msg = """
                <b>💰 Payment Received!</b>

                👤 <b>User:</b> <code>%s</code>
                📦 <b>Order ID:</b> <code>%d</code>
                💳 <b>Method:</b> <code>%s</code>
                💵 <b>Total Amount:</b> <code>%s</code>

                🔐 <b>Transaction:</b>
                <code>%s</code>

                🛠️ Backend: <b>Ecommerce System</b>
                """
                .formatted(
                        order.getUser().getEmail(),
                        order.getId(),
                        order.getPaymentMethod(),
                        order.getTotalPrice().toPlainString(),
                        payment.getTransactionId()
                );

        telegramService.sendHtmlMessage(msg);
    }
}

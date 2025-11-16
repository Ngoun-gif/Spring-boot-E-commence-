package Ecommerce.Application.project.modules.payment.entity;

import Ecommerce.Application.project.modules.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments", schema = "ecommerce")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 20)
    private String method; // CASH, CARD, QRCODE

    @Column(nullable = false, length = 20)
    private String status; // PENDING, PAID, FAILED

    private String transactionId;

    private LocalDateTime createdAt = LocalDateTime.now();
}

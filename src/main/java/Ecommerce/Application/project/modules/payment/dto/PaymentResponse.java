package Ecommerce.Application.project.modules.payment.dto;

import Ecommerce.Application.project.modules.payment.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class PaymentResponse {
    private Long paymentId;
    private Long orderId;
    private PaymentStatus paymentStatus;
    private String transactionId;
    private LocalDateTime createdAt;
    private BigDecimal totalPrice;

}

package Ecommerce.Application.project.modules.payment.dto;

import lombok.*;

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class PaymentResponse {
    private Long paymentId;
    private Long orderId;
    private String method;
    private String status;
    private String transactionId;
}

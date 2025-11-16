package Ecommerce.Application.project.modules.order.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CheckoutRequest {
    private String paymentMethod;  // CASH, CARD, QRCODE
}

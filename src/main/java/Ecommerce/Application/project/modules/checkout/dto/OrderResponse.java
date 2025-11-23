package Ecommerce.Application.project.modules.checkout.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderResponse {

    private Long orderId;
    private BigDecimal totalPrice;
    private String status;
    private String paymentMethod;
    private List<OrderItemResponse> items;
}

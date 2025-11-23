package Ecommerce.Application.project.modules.checkout.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItemResponse {
    private Long productId;
    private String title;
    private int quantity;
    private BigDecimal price;
    private BigDecimal total;
}

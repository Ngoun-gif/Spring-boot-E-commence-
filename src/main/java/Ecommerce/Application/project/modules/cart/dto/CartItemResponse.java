package Ecommerce.Application.project.modules.cart.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CartItemResponse {

    private Long id;
    private Long productId;

    private Integer quantity;
    private BigDecimal price;
    private BigDecimal total;

    private boolean outOfStock;
    private Integer availableStock;
}

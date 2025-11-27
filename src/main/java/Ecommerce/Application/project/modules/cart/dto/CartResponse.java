package Ecommerce.Application.project.modules.cart.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long cartId;
    private BigDecimal totalPrice;
    private List<CartItemResponse> items;
}
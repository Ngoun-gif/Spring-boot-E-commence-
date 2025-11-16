package Ecommerce.Application.project.modules.cart.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AddToCartRequest {
    private Long productId;
    private int quantity;
}

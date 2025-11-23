package Ecommerce.Application.project.modules.cart.dto.cart_item;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AddToCartRequest {
    private Long productId;
    private int quantity;
}

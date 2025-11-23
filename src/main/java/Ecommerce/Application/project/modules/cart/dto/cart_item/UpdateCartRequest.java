package Ecommerce.Application.project.modules.cart.dto.cart_item;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UpdateCartRequest {
    private Long cartItemId;
    private int quantity;
}

package Ecommerce.Application.project.modules.cart.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateCartRequest {
    private Long cartItemId;
    private int quantity;
}

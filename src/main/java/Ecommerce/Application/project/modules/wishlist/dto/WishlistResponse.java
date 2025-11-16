package Ecommerce.Application.project.modules.wishlist.dto;

import Ecommerce.Application.project.modules.products.entity.Product;
import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class WishlistResponse {
    private Long productId;
    private String title;
    private String image;
    private Double price;
}

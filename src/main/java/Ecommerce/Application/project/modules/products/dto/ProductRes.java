package Ecommerce.Application.project.modules.products.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class ProductRes {
    private Long id;
    private String title;
    private Double price;
    private String description;
    private String category;
    private String image;
    private boolean outOfStock;
    private int stockQuantity;
}


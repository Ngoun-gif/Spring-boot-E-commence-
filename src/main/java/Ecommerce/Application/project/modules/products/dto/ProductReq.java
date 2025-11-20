package Ecommerce.Application.project.modules.products.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductReq {
    private String title;
    private Double price;
    private String description;
    private Long categoryId; // FK
    private String image;

}

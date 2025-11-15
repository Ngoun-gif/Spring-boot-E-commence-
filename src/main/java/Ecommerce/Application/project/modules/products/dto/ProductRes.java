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
    private String category;  // category name
    private String image;
    private Double rating;
    private Integer ratingCount;
}

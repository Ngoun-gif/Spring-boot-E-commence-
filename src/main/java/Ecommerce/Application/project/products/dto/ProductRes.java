package Ecommerce.Application.project.products.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ProductRes {
    private Long id;
    private String title;
    private BigDecimal price;
    private String description;
    private String image;
    private Long categoryId;
    private String categoryName;
    private BigDecimal ratingRate;
    private Integer ratingCount;
}

package Ecommerce.Application.project.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductReq {

    @NotBlank
    private String title;

    @NotNull
    private BigDecimal price;

    private String description;
    private String image;
    private Long categoryId;
    private BigDecimal ratingRate;
    private Integer ratingCount;
}

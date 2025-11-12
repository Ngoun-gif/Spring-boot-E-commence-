package Ecommerce.Application.project.products.dto;

import Ecommerce.Application.project.products.entity.Product;
import Ecommerce.Application.project.categories.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductRes toRes(Product p) {
        if (p == null) return null;

        return new ProductRes(
                p.getId(),
                p.getTitle(),
                p.getPrice(),
                p.getDescription(),
                p.getImage(),
                p.getCategory() != null ? p.getCategory().getId() : null,
                p.getCategory() != null ? p.getCategory().getName() : null,
                p.getRatingRate(),
                p.getRatingCount()
        );
    }

    public Product toEntity(ProductReq req, Category category) {
        if (req == null) return null;

        return Product.builder()
                .title(req.getTitle())
                .price(req.getPrice())
                .description(req.getDescription())
                .image(req.getImage())
                .category(category)
                .ratingRate(req.getRatingRate())
                .ratingCount(req.getRatingCount())
                .build();
    }

    public void updateEntity(Product product, ProductReq req, Category category) {
        product.setTitle(req.getTitle());
        product.setPrice(req.getPrice());
        product.setDescription(req.getDescription());
        product.setImage(req.getImage());
        product.setCategory(category);
        product.setRatingRate(req.getRatingRate());
        product.setRatingCount(req.getRatingCount());
    }
}

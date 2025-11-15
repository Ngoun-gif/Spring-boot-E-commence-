package Ecommerce.Application.project.modules.products;

import Ecommerce.Application.project.modules.categories.CategoryRepository;
import Ecommerce.Application.project.modules.categories.entity.Category;
import Ecommerce.Application.project.modules.products.dto.*;
import Ecommerce.Application.project.modules.products.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;
    private final CategoryRepository categoryRepo;

    public List<ProductRes> getAll() {
        return repo.findAll().stream()
                .map(this::toRes)
                .toList();
    }

    public ProductRes getById(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toRes(p);
    }

    public ProductRes create(ProductReq req) {

        Category category = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product p = Product.builder()
                .title(req.getTitle())
                .price(BigDecimal.valueOf(req.getPrice()))
                .description(req.getDescription())
                .image(req.getImage())
                .category(category)
                .rating(req.getRating())
                .ratingCount(req.getRatingCount())
                .build();

        repo.save(p);
        return toRes(p);
    }

    public ProductRes update(Long id, ProductReq req) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        p.setTitle(req.getTitle());
        p.setPrice(BigDecimal.valueOf(req.getPrice()));
        p.setDescription(req.getDescription());
        p.setImage(req.getImage());
        p.setCategory(category);
        p.setRating(req.getRating());
        p.setRatingCount(req.getRatingCount());

        repo.save(p);
        return toRes(p);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    private ProductRes toRes(Product p) {
        return new ProductRes(
                p.getId(),
                p.getTitle(),
                p.getPrice().doubleValue(),
                p.getDescription(),
                p.getCategory().getName(),
                p.getImage(),
                p.getRating(),
                p.getRatingCount()
        );
    }
}

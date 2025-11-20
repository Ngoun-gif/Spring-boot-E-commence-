package Ecommerce.Application.project.modules.products;

import Ecommerce.Application.project.modules.categories.CategoryRepository;
import Ecommerce.Application.project.modules.categories.entity.Category;
import Ecommerce.Application.project.modules.files.ImageDeleteUtil;
import Ecommerce.Application.project.modules.products.dto.*;
import Ecommerce.Application.project.modules.products.entity.Product;
import Ecommerce.Application.project.modules.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;
    private final CategoryRepository categoryRepo;
    private final StockRepository stockRepo;
    private final ImageDeleteUtil imageDeleteUtil;

    // ---------------------------------
    // GET ALL
    // ---------------------------------
    public List<ProductRes> getAll() {
        return repo.findAll()
                .stream()
                .map(this::toRes)
                .toList();
    }

    public ProductRes getById(Long id) {
        return repo.findById(id)
                .map(this::toRes)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // ---------------------------------
    // CREATE
    // ---------------------------------
    public ProductRes create(ProductReq req) {

        Category category = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product p = Product.builder()
                .title(req.getTitle())
                .price(BigDecimal.valueOf(req.getPrice()))
                .description(req.getDescription())
                .image(req.getImage()) // saved by upload API
                .category(category)
                .build();

        repo.save(p);
        return toRes(p);
    }

    // ---------------------------------
    // UPDATE
    // ---------------------------------
    public ProductRes update(Long id, ProductReq req) {

        Product p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // ---------------------------
        // CASE 1: replace image
        // ---------------------------
        if (req.getImage() != null && !req.getImage().equals(p.getImage())) {
            imageDeleteUtil.deleteImage(p.getImage());
            p.setImage(req.getImage());
        }

        // ---------------------------
        // CASE 2: remove image
        // ---------------------------
        else if (req.getImage() == null) {
            imageDeleteUtil.deleteImage(p.getImage());
            p.setImage(null);
        }

        Category category = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        p.setTitle(req.getTitle());
        p.setPrice(BigDecimal.valueOf(req.getPrice()));
        p.setDescription(req.getDescription());
        p.setCategory(category);

        repo.save(p);
        return toRes(p);
    }

    // ---------------------------------
    // DELETE PRODUCT
    // ---------------------------------
    public void delete(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        imageDeleteUtil.deleteImage(p.getImage());
        repo.delete(p);
    }

    // ---------------------------------
    // CONVERT ENTITY -> DTO
    // ---------------------------------
    private ProductRes toRes(Product p) {
        ProductRes res = new ProductRes(
                p.getId(),
                p.getTitle(),
                p.getPrice().doubleValue(),
                p.getDescription(),
                p.getCategory().getName(),
                p.getImage(),
                false,
                0
        );

        var stock = stockRepo.findByProduct(p).orElse(null);
        int qty = (stock != null) ? stock.getQuantity() : 0;

        res.setStockQuantity(qty);
        res.setOutOfStock(qty == 0);

        return res;
    }
}

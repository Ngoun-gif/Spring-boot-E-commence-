package Ecommerce.Application.project.products;

import Ecommerce.Application.project.products.dto.*;
import Ecommerce.Application.project.products.entity.Product;
import Ecommerce.Application.project.categories.CategoryRepository;
import Ecommerce.Application.project.categories.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public List<ProductRes> getAll() {
        return productRepository.findAll().stream()
                .map(productMapper::toRes)
                .toList();
    }

    public ProductRes getById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toRes)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public ProductRes create(ProductReq req) {
        Category category = null;
        if (req.getCategoryId() != null) {
            category = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        Product p = productMapper.toEntity(req, category);
        productRepository.save(p);
        return productMapper.toRes(p);
    }

    public ProductRes update(Long id, ProductReq req) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = null;
        if (req.getCategoryId() != null) {
            category = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }

        productMapper.updateEntity(p, req, category);
        productRepository.save(p);
        return productMapper.toRes(p);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}

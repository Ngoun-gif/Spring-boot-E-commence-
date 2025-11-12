package Ecommerce.Application.project.products;

import Ecommerce.Application.project.products.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductRes> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public ProductRes getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductRes create(@RequestBody ProductReq req) {
        return productService.create(req);
    }

    @PutMapping("/{id}")
    public ProductRes update(@PathVariable Long id, @RequestBody ProductReq req) {
        return productService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }
}

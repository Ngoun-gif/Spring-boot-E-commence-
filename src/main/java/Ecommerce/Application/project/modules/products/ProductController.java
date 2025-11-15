package Ecommerce.Application.project.modules.products;

import Ecommerce.Application.project.modules.products.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    @GetMapping
    public List<ProductRes> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ProductRes getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductRes create(@RequestBody ProductReq req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public ProductRes update(@PathVariable Long id, @RequestBody ProductReq req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

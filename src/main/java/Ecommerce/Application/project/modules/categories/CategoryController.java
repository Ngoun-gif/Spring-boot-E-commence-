package Ecommerce.Application.project.modules.categories;

import Ecommerce.Application.project.modules.categories.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping
    public List<CategoryRes> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public CategoryRes getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryRes create(@RequestBody CategoryReq req) {
        return service.create(req);
    }

    @PutMapping("/{id}")
    public CategoryRes update(@PathVariable Long id, @RequestBody CategoryReq req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

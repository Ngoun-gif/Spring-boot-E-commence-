package Ecommerce.Application.project.categories;

import Ecommerce.Application.project.categories.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryRes> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryRes getById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryRes create(@RequestBody CategoryReq req) {
        return categoryService.create(req);
    }

    @PutMapping("/{id}")
    public CategoryRes update(@PathVariable Long id, @RequestBody CategoryReq req) {
        return categoryService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}

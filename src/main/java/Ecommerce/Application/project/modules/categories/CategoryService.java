package Ecommerce.Application.project.modules.categories;

import Ecommerce.Application.project.modules.categories.dto.*;
import Ecommerce.Application.project.modules.categories.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repo;

    public List<CategoryRes> getAll() {
        return repo.findAll().stream()
                .map(c -> new CategoryRes(c.getId(), c.getName(), c.isActive()))
                .toList();
    }

    public CategoryRes getById(Long id) {
        Category c = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return new CategoryRes(c.getId(), c.getName(), c.isActive());
    }

    public CategoryRes create(CategoryReq req) {
        if (repo.findByName(req.getName()).isPresent()) {
            throw new RuntimeException("Category already exists!");
        }

        Category c = new Category();
        c.setName(req.getName());
        c.setActive(req.isActive());
        repo.save(c);

        return new CategoryRes(c.getId(), c.getName(), c.isActive());
    }

    public CategoryRes update(Long id, CategoryReq req) {
        Category c = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        c.setName(req.getName());
        c.setActive(req.isActive());
        repo.save(c);

        return new CategoryRes(c.getId(), c.getName(), c.isActive());
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        repo.deleteById(id);
    }
}

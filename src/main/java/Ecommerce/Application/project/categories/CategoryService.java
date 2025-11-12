package Ecommerce.Application.project.categories;

import Ecommerce.Application.project.categories.dto.*;
import Ecommerce.Application.project.categories.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryRes> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toRes)
                .toList();
    }

    public CategoryRes getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return CategoryMapper.toRes(category);
    }

    public CategoryRes create(CategoryReq req) {
        Category category = CategoryMapper.toEntity(req);
        categoryRepository.save(category);
        return CategoryMapper.toRes(category);
    }

    public CategoryRes update(Long id, CategoryReq req) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(req.getName());
        category.setActive(req.isActive());
        categoryRepository.save(category);
        return CategoryMapper.toRes(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}

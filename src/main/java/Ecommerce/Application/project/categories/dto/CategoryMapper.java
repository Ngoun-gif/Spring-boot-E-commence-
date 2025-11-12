package Ecommerce.Application.project.categories.dto;

import Ecommerce.Application.project.categories.entity.Category;

public class CategoryMapper {

    public static CategoryRes toRes(Category entity) {
        return CategoryRes.builder()
                .id(entity.getId())
                .name(entity.getName())
                .active(entity.isActive())
                .build();
    }

    public static Category toEntity(CategoryReq req) {
        Category category = new Category();
        category.setName(req.getName());
        category.setActive(req.isActive());
        return category;
    }
}

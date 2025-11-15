package Ecommerce.Application.project.modules.categories;

import Ecommerce.Application.project.modules.categories.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}

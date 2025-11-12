package Ecommerce.Application.project.products;

import Ecommerce.Application.project.products.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory_Id(Long categoryId);

    Page<Product> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

    boolean existsByTitleAndCategory_Id(String title, Long categoryId);
}

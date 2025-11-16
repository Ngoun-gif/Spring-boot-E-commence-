package Ecommerce.Application.project.modules.products;

import Ecommerce.Application.project.modules.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {



}

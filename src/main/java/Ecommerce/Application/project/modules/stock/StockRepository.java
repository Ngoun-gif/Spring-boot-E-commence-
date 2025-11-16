package Ecommerce.Application.project.modules.stock;

import Ecommerce.Application.project.modules.stock.entity.Stock;
import Ecommerce.Application.project.modules.products.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByProduct(Product product);
}

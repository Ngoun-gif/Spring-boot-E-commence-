package Ecommerce.Application.project.modules.stock.repository;

import Ecommerce.Application.project.modules.stock.entity.ProductImport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImportRepository extends JpaRepository<ProductImport, Long> {
}

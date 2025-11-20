package Ecommerce.Application.project.modules.stock.repository;

import Ecommerce.Application.project.modules.stock.entity.StockImport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockImportRepository extends JpaRepository<StockImport, Long> {
}

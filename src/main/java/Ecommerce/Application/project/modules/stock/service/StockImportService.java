package Ecommerce.Application.project.modules.stock.service;

import Ecommerce.Application.project.modules.products.entity.Product;
import Ecommerce.Application.project.modules.products.ProductRepository;
import Ecommerce.Application.project.modules.stock.dto.StockImportRequest;
import Ecommerce.Application.project.modules.stock.entity.StockImport;
import Ecommerce.Application.project.modules.stock.repository.StockImportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockImportService {

    private final ProductRepository productRepository;
    private final StockImportRepository importRepository;
    private final StockService stockService;

    // FULL IMPORT PROCESS
    public StockImport importProduct(StockImportRequest req) {

        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 1. Save import history
        StockImport imp = StockImport.builder()
                .product(product)
                .quantity(req.getQuantity())
                .remark(req.getRemark())
                .build();

        importRepository.save(imp);

        // 2. Update total stock
        stockService.importStock(req.getQuantity(), product);

        return imp;
    }

    // GET all imports
    public List<StockImport> getAllImports() {
        return importRepository.findAll();
    }
}

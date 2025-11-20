package Ecommerce.Application.project.modules.stock.service;

import Ecommerce.Application.project.modules.products.entity.Product;
import Ecommerce.Application.project.modules.products.ProductRepository;
import Ecommerce.Application.project.modules.stock.dto.ProductImportRequest;
import Ecommerce.Application.project.modules.stock.entity.ProductImport;
import Ecommerce.Application.project.modules.stock.repository.ProductImportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductImportService {

    private final ProductRepository productRepository;
    private final ProductImportRepository importRepository;
    private final StockService stockService;

    // FULL IMPORT PROCESS
    public ProductImport importProduct(ProductImportRequest req) {

        Product product = productRepository.findById(req.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 1. Save import history
        ProductImport imp = ProductImport.builder()
                .product(product)
                .quantity(req.getQuantity())
                .remark(req.getRemark())
                .build();
        importRepository.save(imp);

        // 2. Update stock total
        stockService.importStock(req.getQuantity(), product);

        return imp;
    }
}

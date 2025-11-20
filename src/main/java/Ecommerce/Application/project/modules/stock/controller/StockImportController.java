package Ecommerce.Application.project.modules.stock.controller;

import Ecommerce.Application.project.modules.stock.dto.ProductImportRequest;
import Ecommerce.Application.project.modules.stock.entity.ProductImport;
import Ecommerce.Application.project.modules.stock.service.ProductImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product-import")
@RequiredArgsConstructor
public class ProductImportController {

    private final ProductImportService importService;

    @PostMapping
    public ProductImport importProduct(@RequestBody ProductImportRequest req) {
        return importService.importProduct(req);
    }
}

package Ecommerce.Application.project.modules.stock.controller;

import Ecommerce.Application.project.modules.stock.dto.StockImportRequest;
import Ecommerce.Application.project.modules.stock.entity.StockImport;
import Ecommerce.Application.project.modules.stock.service.StockImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockImportController {

    private final StockImportService importService;

    // Import stock
    @PostMapping("/import")
    public StockImport importStock(@RequestBody StockImportRequest req) {
        return importService.importProduct(req);
    }

    // Import history
    @GetMapping("/imports")
    public List<StockImport> getImportHistory() {
        return importService.getAllImports();
    }
}

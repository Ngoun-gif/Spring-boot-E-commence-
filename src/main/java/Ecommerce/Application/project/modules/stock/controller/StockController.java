package Ecommerce.Application.project.modules.stock.controller;

import Ecommerce.Application.project.modules.stock.entity.Stock;
import Ecommerce.Application.project.modules.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    // 1️⃣ GET all (dashboard)
    @GetMapping
    public List<Stock> getAll() {
        return stockService.getAllStocks();
    }

    // 2️⃣ GET by productId
    @GetMapping("/{productId}")
    public Stock getStock(@PathVariable Long productId) {
        return stockService.getStock(productId);
    }

    // 3️⃣ SET stock manually
    @PutMapping("/set")
    public Stock setStock(@RequestParam Long productId,
                          @RequestParam int quantity) {
        return stockService.setStock(productId, quantity);
    }
}

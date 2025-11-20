package Ecommerce.Application.project.modules.stock;

import Ecommerce.Application.project.modules.stock.entity.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping("/set")
    public Stock setStock(@RequestParam Long productId,
                          @RequestParam int quantity) {
        return stockService.createOrUpdate(productId, quantity);
    }

    @GetMapping("/{productId}")
    public Stock getStock(@PathVariable Long productId) {
        return stockService.getStock(productId);
    }
}

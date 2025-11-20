package Ecommerce.Application.project.modules.stock.service;

import Ecommerce.Application.project.modules.products.entity.Product;
import Ecommerce.Application.project.modules.products.ProductRepository;
import Ecommerce.Application.project.modules.stock.dto.StockResponse;
import Ecommerce.Application.project.modules.stock.entity.Stock;
import Ecommerce.Application.project.modules.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductRepository productRepository;
    private final StockRepository stockRepository;

    private Stock getOrCreate(Product product) {
        return stockRepository.findByProductId(product.getId())
                .orElseGet(() -> Stock.builder()
                        .product(product)
                        .quantity(0)
                        .build()
                );
    }

    // 1️⃣ Import stock (optional future use)
    public StockResponse importStock(int quantity, Product product) {
        Stock stock = getOrCreate(product);

        int oldStock = stock.getQuantity();
        int newStock = oldStock + quantity;

        stock.setQuantity(newStock);
        stock.setUpdatedAt(LocalDateTime.now());
        stockRepository.save(stock);

        return new StockResponse(
                product.getId(),
                oldStock,
                quantity,
                newStock
        );
    }

    // 2️⃣ Manual stock set
    public Stock setStock(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Stock stock = getOrCreate(product);
        stock.setQuantity(quantity);
        stock.setUpdatedAt(LocalDateTime.now());

        return stockRepository.save(stock);
    }

    // 3️⃣ Get one product stock
    public Stock getStock(Long productId) {
        return stockRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
    }

    // 4️⃣ Get ALL stock (dashboard)
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
}

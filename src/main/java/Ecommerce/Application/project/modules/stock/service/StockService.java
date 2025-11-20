package Ecommerce.Application.project.modules.stock;

import Ecommerce.Application.project.modules.stock.entity.Stock;
import Ecommerce.Application.project.modules.stock.StockRepository;
import Ecommerce.Application.project.modules.products.ProductRepository;
import Ecommerce.Application.project.modules.products.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    public Stock createOrUpdate(Long productId, int qty) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Stock stock = stockRepository.findByProduct(product)
                .orElse(new Stock());

        stock.setProduct(product);
        stock.setQuantity(qty);
        stock.setUpdatedAt(java.time.LocalDateTime.now());

        return stockRepository.save(stock);
    }

    public Stock getStock(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return stockRepository.findByProduct(product)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
    }
}

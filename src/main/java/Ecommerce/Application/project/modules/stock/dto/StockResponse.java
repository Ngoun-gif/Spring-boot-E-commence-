package Ecommerce.Application.project.modules.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockResponse {
    private Long productId;
    private int oldStock;
    private int changed;
    private int newStock;
}

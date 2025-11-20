package Ecommerce.Application.project.modules.stock.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StockRequest {
    private Long productId;
    private int quantity;
}

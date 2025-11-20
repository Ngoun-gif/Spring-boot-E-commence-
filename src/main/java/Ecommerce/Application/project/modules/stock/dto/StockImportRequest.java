package Ecommerce.Application.project.modules.stock.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductImportRequest {
    private Long productId;
    private int quantity;
    private String remark;
}

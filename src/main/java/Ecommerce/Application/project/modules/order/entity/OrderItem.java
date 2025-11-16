package Ecommerce.Application.project.modules.order.entity;

import Ecommerce.Application.project.modules.products.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items", schema = "ecommerce")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Reference the main order
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Snapshot of product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;

    private BigDecimal price;

    private BigDecimal total;
}

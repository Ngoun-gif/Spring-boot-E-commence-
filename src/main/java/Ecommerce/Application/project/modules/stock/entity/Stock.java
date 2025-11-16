package Ecommerce.Application.project.modules.stock.entity;

import Ecommerce.Application.project.modules.products.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stocks", schema = "ecommerce")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    private int quantity;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}

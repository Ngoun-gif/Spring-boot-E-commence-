package Ecommerce.Application.project.categories.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories", schema = "ecommerce")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private boolean active = true;
}

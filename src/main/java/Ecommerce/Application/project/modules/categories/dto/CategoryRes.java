package Ecommerce.Application.project.modules.categories.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CategoryRes {
    private Long id;
    private String name;
    private boolean active;
}

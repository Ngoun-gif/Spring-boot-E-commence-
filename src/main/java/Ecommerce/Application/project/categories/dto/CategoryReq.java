package Ecommerce.Application.project.categories.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryReq {

    @NotBlank(message = "Category name is required")
    private String name;

    private boolean active = true;
}

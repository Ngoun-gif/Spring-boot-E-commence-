package Ecommerce.Application.project.modules.categories.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CategoryReq {
    private String name;
    private boolean active = true;
}

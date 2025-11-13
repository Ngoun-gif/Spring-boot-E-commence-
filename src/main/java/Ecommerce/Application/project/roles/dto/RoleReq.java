package Ecommerce.Application.project.roles.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleReq {
    @NotBlank
    private String name;
}

package Ecommerce.Application.project.modules.users.dto;

import Ecommerce.Application.project.modules.users.entity.User;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserRes toRes(User user) {
        return new UserRes(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                user.getPhone(),
                user.isActive(),
                user.getCreatedAt(),
                user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet())
        );
    }
}

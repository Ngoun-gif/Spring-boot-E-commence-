package Ecommerce.Application.project.roles.dto;

import Ecommerce.Application.project.roles.entity.Role;

public class RoleMapper {

    public static RoleRes toRes(Role role) {
        return new RoleRes(role.getId(), role.getName());
    }

    public static Role toEntity(RoleReq req) {
        return Role.builder()
                .name(req.getName())
                .build();
    }

    public static void update(Role role, RoleReq req) {
        role.setName(req.getName());
    }
}

package Ecommerce.Application.project.modules.roles;


import Ecommerce.Application.project.modules.roles.dto.*;
import Ecommerce.Application.project.modules.roles.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public List<RoleRes> getAll() {
        return roleRepository.findAll()
                .stream()
                .map(RoleMapper::toRes)
                .toList();
    }

    public RoleRes getById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return RoleMapper.toRes(role);
    }

    public RoleRes create(RoleReq req) {
        if (roleRepository.findByName(req.getName()).isPresent()) {
            throw new RuntimeException("Role already exists");
        }
        Role role = RoleMapper.toEntity(req);
        roleRepository.save(role);
        return RoleMapper.toRes(role);
    }

    public RoleRes update(Long id, RoleReq req) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        RoleMapper.update(role, req);
        roleRepository.save(role);
        return RoleMapper.toRes(role);
    }

    public void delete(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found");
        }
        roleRepository.deleteById(id);
    }
}

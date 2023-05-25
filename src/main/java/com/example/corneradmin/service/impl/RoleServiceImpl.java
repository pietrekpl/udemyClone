package com.example.corneradmin.service.impl;

import com.example.corneradmin.model.Role;
import com.example.corneradmin.repository.RoleRepository;
import com.example.corneradmin.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role createRole(String roleName) {
        return roleRepository.save(new Role(roleName));
    }
}

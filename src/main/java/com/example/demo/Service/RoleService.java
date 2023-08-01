package com.example.demo.Service;

import com.example.demo.Entity.ERole;
import com.example.demo.Entity.Roles;

import java.util.Optional;

public interface RoleService {
    Optional<Roles> findByRoleName(ERole roleName);
}

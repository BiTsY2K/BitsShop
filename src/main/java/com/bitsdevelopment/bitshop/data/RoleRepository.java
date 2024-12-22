package com.bitsdevelopment.bitshop.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bitsdevelopment.bitshop.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(String role);
}

package com.example.licentaRestaurante.repositories;

import com.example.licentaRestaurante.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}

package com.example.licentaRestaurante.repositories;

import com.example.licentaRestaurante.entities.DietaryRestriction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DietaryRestrictionRepository extends JpaRepository<DietaryRestriction, String> {
    DietaryRestriction findByName(String name);
}
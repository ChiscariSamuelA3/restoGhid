package com.example.licentaRestaurante.repositories;

import com.example.licentaRestaurante.entities.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
}
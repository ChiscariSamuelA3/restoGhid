package com.example.licentaRestaurante.repositories;

import com.example.licentaRestaurante.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
package com.example.licentaRestaurante.repositories;

import com.example.licentaRestaurante.entities.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface CuisineRepository extends JpaRepository<Cuisine, String> {
    Cuisine findByName(String name);
}
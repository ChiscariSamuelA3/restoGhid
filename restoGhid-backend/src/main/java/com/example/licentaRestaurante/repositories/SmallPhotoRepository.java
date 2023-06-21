package com.example.licentaRestaurante.repositories;

import com.example.licentaRestaurante.entities.Small;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmallPhotoRepository extends JpaRepository<Small, Long> {
}
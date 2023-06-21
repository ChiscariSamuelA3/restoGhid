package com.example.licentaRestaurante.repositories;

import com.example.licentaRestaurante.entities.Original;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OriginalPhotoRepository extends JpaRepository<Original, Long> {
}
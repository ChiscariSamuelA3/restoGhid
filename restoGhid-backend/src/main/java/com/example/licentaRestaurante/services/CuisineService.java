package com.example.licentaRestaurante.services;

import com.example.licentaRestaurante.entities.Cuisine;
import com.example.licentaRestaurante.models.cuisines.AllCuisines;
import com.example.licentaRestaurante.repositories.CuisineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CuisineService {
    private final CuisineRepository cuisineRepository;

    @Autowired
    public CuisineService(CuisineRepository cuisineRepository) {
        this.cuisineRepository = cuisineRepository;
    }

    public AllCuisines getCuisines() {
        var allCuisines = new AllCuisines();
        try {
            List<Cuisine> cuisines = cuisineRepository.findAll();
            allCuisines.setData(cuisines);
            allCuisines.setStatusCode(200);
        } catch (Exception e) {
            allCuisines.setError("Cuisines not found");
            allCuisines.setStatusCode(404);
        }
        return allCuisines;
    }
}

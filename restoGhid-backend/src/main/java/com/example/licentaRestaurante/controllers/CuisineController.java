package com.example.licentaRestaurante.controllers;

import com.example.licentaRestaurante.models.cuisines.AllCuisines;
import com.example.licentaRestaurante.services.CuisineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/cuisines")
public class CuisineController {
    private final CuisineService cuisineService;

    @Autowired
    public CuisineController(CuisineService cuisineService) {
        this.cuisineService = cuisineService;
    }

    @GetMapping
    public AllCuisines getCuisines() {
        return cuisineService.getCuisines();
    }
}

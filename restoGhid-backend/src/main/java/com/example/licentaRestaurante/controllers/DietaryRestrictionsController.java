package com.example.licentaRestaurante.controllers;

import com.example.licentaRestaurante.models.restrictions.AllRestrictions;
import com.example.licentaRestaurante.services.DietaryRestrictionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/restrictions")
public class DietaryRestrictionsController {
    private final DietaryRestrictionsService dietaryRestrictionsService;

    @Autowired
    public DietaryRestrictionsController(DietaryRestrictionsService dietaryRestrictionsService) {
        this.dietaryRestrictionsService = dietaryRestrictionsService;
    }

    @GetMapping
    public AllRestrictions getAllRestrictions() {
        return dietaryRestrictionsService.getAllRestrictions();
    }
}

package com.example.licentaRestaurante.services;

import com.example.licentaRestaurante.entities.DietaryRestriction;
import com.example.licentaRestaurante.models.restrictions.AllRestrictions;
import com.example.licentaRestaurante.repositories.DietaryRestrictionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DietaryRestrictionsService {
    private final DietaryRestrictionRepository dietaryRestrictionRepository;

    @Autowired
    public DietaryRestrictionsService(DietaryRestrictionRepository dietaryRestrictionRepository) {
        this.dietaryRestrictionRepository = dietaryRestrictionRepository;
    }

    public AllRestrictions getAllRestrictions() {
        var allRestrictions = new AllRestrictions();
        try {
            List<DietaryRestriction> restrictions = dietaryRestrictionRepository.findAll();
            allRestrictions.setData(restrictions);
            allRestrictions.setStatusCode(200);
        } catch (Exception e) {
            allRestrictions.setError("Restrictions not found");
            allRestrictions.setStatusCode(404);
        }
        return allRestrictions;
    }
}

package com.example.licentaRestaurante.services;

import com.example.licentaRestaurante.entities.Subcategory;
import com.example.licentaRestaurante.repositories.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubcategoryService {
    private final SubcategoryRepository subcategoryRepository;

    @Autowired
    public SubcategoryService(SubcategoryRepository subcategoryRepository) {
        this.subcategoryRepository = subcategoryRepository;
    }

    public List<String> getAllSubcategoriesKeys() {
        var subcategories = subcategoryRepository.findAll();
        var subcategoriesKeys = new ArrayList<String>();
        for (Subcategory subcategory : subcategories) {
            subcategoriesKeys.add(subcategory.getKey());
        }

        return subcategoriesKeys;
    }
}

package com.example.licentaRestaurante.controllers;

import com.example.licentaRestaurante.entities.Root;
import com.example.licentaRestaurante.services.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/results")
public class ResultController {
    private final ResultService resultService;

    @Autowired
    public ResultController(ResultService restaurantService) {
        this.resultService = restaurantService;
    }

    @GetMapping("/test")
    public Root test() throws IOException {
        return resultService.getRestaurantsData();
    }
}
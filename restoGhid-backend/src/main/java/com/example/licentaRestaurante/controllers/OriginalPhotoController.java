package com.example.licentaRestaurante.controllers;

import com.example.licentaRestaurante.models.originalphoto.SingleOriginalPhoto;
import com.example.licentaRestaurante.services.OriginalPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/photos")
public class OriginalPhotoController {
    private final OriginalPhotoService originalPhotoService;

    @Autowired
    public OriginalPhotoController(OriginalPhotoService originalPhotoService) {
        this.originalPhotoService = originalPhotoService;
    }

    @GetMapping("{locationId}")
    public SingleOriginalPhoto getOriginalPhotoByLocationId(@PathVariable String locationId) {
        return originalPhotoService.findOriginalPhoto(locationId);
    }
}

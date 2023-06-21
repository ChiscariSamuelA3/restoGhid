package com.example.licentaRestaurante.services;

import com.example.licentaRestaurante.entities.Original;
import com.example.licentaRestaurante.models.originalphoto.SingleOriginalPhoto;
import com.example.licentaRestaurante.repositories.OriginalPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OriginalPhotoService {
    private final OriginalPhotoRepository originalPhotoRepository;

    @Autowired
    public OriginalPhotoService(OriginalPhotoRepository originalPhotoRepository) {
        this.originalPhotoRepository = originalPhotoRepository;
    }

    public SingleOriginalPhoto findOriginalPhoto(String locationId) {
        var singleOriginalPhoto = new SingleOriginalPhoto();
        try {
            // get all original photos from the database
            var originalPhotos = originalPhotoRepository.findAll();
            // get the original photo with the restaurant id that matches the locationId
            Original photo = originalPhotos.stream().filter(original -> original.getRestaurant_id().equals(locationId)).findFirst().get();

            singleOriginalPhoto.setData(photo);
            singleOriginalPhoto.setStatusCode(200);
        } catch (Exception e) {
            singleOriginalPhoto.setError("OriginalPhoto not found");
            singleOriginalPhoto.setStatusCode(404);
        }
        return singleOriginalPhoto;
    }
}

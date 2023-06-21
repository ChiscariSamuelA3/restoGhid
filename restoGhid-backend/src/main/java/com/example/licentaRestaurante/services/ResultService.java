package com.example.licentaRestaurante.services;

import com.example.licentaRestaurante.entities.*;
import com.example.licentaRestaurante.repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class ResultService {
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final OriginalPhotoRepository originalPhotoRepository;
    private final SmallPhotoRepository smallPhotoRepository;

    @Autowired
    public ResultService(RestaurantRepository restaurantRepository, CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, OriginalPhotoRepository originalPhotoRepository, SmallPhotoRepository smallPhotoRepository) {
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.originalPhotoRepository = originalPhotoRepository;
        this.smallPhotoRepository = smallPhotoRepository;
    }

    public Root getRestaurantsData() throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("language", "ro_RO")
                .add("limit", "100")
                .add("location_id", "304060")
                .add("currency", "RON")
                .build();

        Request request = new Request.Builder()
                .url("https://worldwide-restaurants.p.rapidapi.com/search")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("X-RapidAPI-Key", "b0fe178c37msh0458cfad78d45f9p145aecjsn1aef49f3947e")
                .addHeader("X-RapidAPI-Host", "worldwide-restaurants.p.rapidapi.com")
                .build();

        Response response = client.newCall(request).execute();
        String json = Objects.requireNonNull(response.body()).string();
        ObjectMapper mapper = new ObjectMapper();

        Root root = mapper.readValue(json, Root.class);

        for (int i = 0; i < root.getResults().getData().size(); i++) {
            Restaurant data = root.getResults().getData().get(i);
            Category category = data.getCategory();
            categoryRepository.save(category);

            data.setCategoryKey(category.getKey());

            var subcategories = data.getSubcategory();
            if (!subcategories.isEmpty()) {
                Subcategory subcategory = subcategories.get(0);
                subcategoryRepository.save(subcategory);

                data.setSubcategoryKey(subcategory.getKey());
            }

            restaurantRepository.save(data);

            Original original = data.getPhoto().getImages().getOriginal();
            original.setRestaurant_id(data.getLocationId());
            originalPhotoRepository.save(original);

            Small small = data.getPhoto().getImages().getSmall();

            small.setRestaurant_id(data.getLocationId());
            smallPhotoRepository.save(small);
        }

        return mapper.readValue(json, Root.class);
    }
}

package com.example.licentaRestaurante.models.restaurants;

import com.example.licentaRestaurante.entities.Restaurant;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class AllRestaurants {
    List<Restaurant> data;
    private String error = "";
    private int statusCode = 500;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllRestaurants that = (AllRestaurants) o;
        return statusCode == that.statusCode && Objects.equals(data, that.data) && Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, error, statusCode);
    }
}

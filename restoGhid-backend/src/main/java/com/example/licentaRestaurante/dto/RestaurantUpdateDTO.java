package com.example.licentaRestaurante.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class RestaurantUpdateDTO {
    private String name;
    private String description;
    private String address;
    private String phone;
    private Integer totalSeats;
}

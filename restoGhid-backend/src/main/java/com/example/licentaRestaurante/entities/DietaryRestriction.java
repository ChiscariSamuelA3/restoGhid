package com.example.licentaRestaurante.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "dietary_restrictions")
@AllArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DietaryRestriction {
    @Id
    @Column(name = "key", nullable = false)
    @JsonProperty("key")
    String key;

    @JsonProperty("name")
    String name;

    @ManyToMany(mappedBy = "dietaryRestrictions")
    @JsonIgnore
    private Set<Restaurant> restaurants = new HashSet<>();
}

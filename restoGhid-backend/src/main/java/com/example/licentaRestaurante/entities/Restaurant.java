package com.example.licentaRestaurante.entities;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "restaurant")
@AllArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
public class Restaurant {
    @Id
    @Column(name = "location_id", nullable = false)
    @JsonProperty("location_id")
    String locationId;

    @JsonProperty("name")
    String name;

    @JsonProperty("latitude")
    String latitude;

    @JsonProperty("longitude")
    String longitude;

    @JsonProperty("num_reviews")
    String num_reviews;

    @JsonProperty("photo")
    @JsonInclude()
    @Transient
    Photo photo;

    @JsonProperty("ranking_geo")
    String ranking_geo;

    @JsonProperty("rating")
    String rating;

    @JsonProperty("is_closed")
    boolean isClosed;

    @JsonProperty("price")
    String price;

    @JsonProperty("description")
    String description;

    @JsonProperty("category")
    @JsonInclude()
    @Transient
    Category category;

    @Column(name = "category_key")
    private String categoryKey;

    @JsonProperty("subcategory")
    @JsonInclude()
    @Transient
    ArrayList<Subcategory> subcategory;

    @Column(name = "subcategory_key")
    private String subcategoryKey;

    @JsonProperty("phone")
    String phone;

    @JsonProperty("website")
    String website;

    @JsonProperty("email")
    String email;

    @JsonProperty("address")
    String address;

    @JsonProperty("cluster")
    Integer cluster;

    @Column(name = "manager_username")
    String managerUsername;

    @Column(name = "total_seats")
    @JsonProperty("total_seats")
    Integer totalSeats;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "restaurant_cuisine",
            joinColumns = {@JoinColumn(name = "restaurant_id")},
            inverseJoinColumns = {@JoinColumn(name = "cuisine_key")})
    @JsonProperty("cuisine")
    private Set<Cuisine> cuisines = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "restaurant_dietary",
            joinColumns = {@JoinColumn(name = "restaurant_id")},
            inverseJoinColumns = {@JoinColumn(name = "dietary_key")})
    @JsonProperty("dietary_restrictions")
    private Set<DietaryRestriction> dietaryRestrictions = new HashSet<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @JsonManagedReference("restaurant-booking")
    private List<Booking> bookings;

    // add booking
    public void addBooking(Booking booking) {
        if (bookings == null) {
            bookings = new ArrayList<>();
        }
        bookings.add(booking);
        booking.setRestaurant(this);
    }

    // remove booking
    public void removeBooking(Booking booking) {
        if (bookings != null) {
            bookings.remove(booking);
            booking.setRestaurant(null);
        }
    }
}

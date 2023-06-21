package com.example.licentaRestaurante.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "booking")
@AllArgsConstructor
@RequiredArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference("restaurant-booking")
    private Restaurant restaurant;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "user_phone")
    private String userPhone;

    @Column(name = "username")
    private String username;

    @Column(name ="latitude")
    private String latitude;

    @Column(name ="longitude")
    private String longitude;

    @Column(name ="phone_number")
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-booking")
    private User user;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "meal_type")
    private Integer mealType;

    @Column(name = "selected_interval")
    private Integer selectedInterval;

    @Column(name = "booked_seats")
    private Integer bookedSeats;

    @Column(name = "total_seats")
    private Integer totalSeats;
}


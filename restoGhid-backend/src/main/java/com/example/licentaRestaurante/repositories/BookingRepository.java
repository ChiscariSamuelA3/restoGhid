package com.example.licentaRestaurante.repositories;

import com.example.licentaRestaurante.entities.Booking;
import com.example.licentaRestaurante.entities.Restaurant;
import com.example.licentaRestaurante.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRestaurantAndDateAndMealTypeAndSelectedInterval(Restaurant restaurant, LocalDate date, Integer period, Integer selectedInterval);

    List<Booking> findByRestaurant(Restaurant restaurant);

    List<Booking> findByUser(User user);

    @Modifying
    @Transactional
    @Query("delete from Booking b where b.date < CURRENT_DATE")
    void deleteOutdatedBookings();
}

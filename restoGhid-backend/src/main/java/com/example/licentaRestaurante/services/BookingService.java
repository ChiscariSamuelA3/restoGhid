package com.example.licentaRestaurante.services;

import com.example.licentaRestaurante.entities.Booking;
import com.example.licentaRestaurante.entities.Restaurant;
import com.example.licentaRestaurante.entities.User;
import com.example.licentaRestaurante.models.bookings.SingleBooking;
import com.example.licentaRestaurante.repositories.BookingRepository;
import com.example.licentaRestaurante.repositories.RestaurantRepository;
import com.example.licentaRestaurante.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public SingleBooking createBooking(String restaurantId, String username, LocalDate date, Integer period, Integer selectedInterval, Integer numOfSeats, Integer totalSeats, String userPhone) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid restaurant id."));

        User user = userRepository.findByUsername(username);

        if (user == null) {
            return new SingleBooking(null, "Invalid user id.", 400);
        }

        // Find all reservations for this restaurant, on this day, period and time slot
        List<Booking> existingBookings = bookingRepository.findByRestaurantAndDateAndMealTypeAndSelectedInterval(restaurant, date, period, selectedInterval);

        // Calculates the total number of seats already reserved
        int totalBookedSeats = existingBookings.stream()
                .mapToInt(Booking::getBookedSeats)
                .sum();

        // Check if there are still places available
        if (totalBookedSeats + numOfSeats > totalSeats) {
            //throw new IllegalArgumentException("There are not enough available seats.");
            return new SingleBooking(null, "There are not enough available seats.", 400);
        }

        Booking booking = new Booking();
        booking.setRestaurant(restaurant);
        booking.setRestaurantName(restaurant.getName());
        booking.setPhoneNumber(restaurant.getPhone());
        booking.setLatitude(restaurant.getLatitude());
        booking.setLongitude(restaurant.getLongitude());
        booking.setUser(user);
        booking.setDate(date);
        booking.setMealType(period);
        booking.setSelectedInterval(selectedInterval);
        booking.setBookedSeats(numOfSeats);
        booking.setTotalSeats(totalSeats);
        booking.setUserPhone(userPhone);
        booking.setUsername(user.getUsername());

        restaurant.addBooking(booking);
        user.addBooking(booking);

        return new SingleBooking(bookingRepository.save(booking), "", 200);
    }
}
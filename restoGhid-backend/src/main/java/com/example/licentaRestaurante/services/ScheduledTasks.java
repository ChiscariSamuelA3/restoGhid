package com.example.licentaRestaurante.services;

import com.example.licentaRestaurante.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledTasks {
    private final BookingRepository bookingRepository;

    @Autowired
    public ScheduledTasks(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Scheduled(cron = "0 10 9,12,18 * * ?")
    public void deleteOutdatedBookings() {
        bookingRepository.deleteOutdatedBookings();
    }
}

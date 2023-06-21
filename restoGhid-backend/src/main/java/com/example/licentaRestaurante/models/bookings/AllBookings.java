package com.example.licentaRestaurante.models.bookings;

import com.example.licentaRestaurante.entities.Booking;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class AllBookings {
    private List<Booking> data;
    private String error = "";
    private int statusCode = 500;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllBookings that = (AllBookings) o;
        return statusCode == that.statusCode && Objects.equals(data, that.data) && Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, error, statusCode);
    }
}

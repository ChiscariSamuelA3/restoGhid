package com.example.licentaRestaurante.models.users;

import com.example.licentaRestaurante.entities.User;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private User data = new User();
    private String error = "";
    private int statusCode = 500;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return statusCode == that.statusCode && Objects.equals(data, that.data) && Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, error, statusCode);
    }
}
package com.example.licentaRestaurante.models.users;

import com.example.licentaRestaurante.entities.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class AllUsers {
    private List<User> data = new ArrayList<>();
    private String error = "";
    private int statusCode = 500;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AllUsers allUsers = (AllUsers) o;
        return statusCode == allUsers.statusCode && Objects.equals(data, allUsers.data) && Objects.equals(error, allUsers.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, error, statusCode);
    }
}
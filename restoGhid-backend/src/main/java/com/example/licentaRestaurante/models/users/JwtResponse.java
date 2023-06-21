package com.example.licentaRestaurante.models.users;

import com.example.licentaRestaurante.entities.LoginResponse;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class JwtResponse {
    private LoginResponse data;
    private String error = "";
    private int statusCode = 500;
}

package com.example.licentaRestaurante.entities;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}


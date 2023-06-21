package com.example.licentaRestaurante.entities;

import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Boolean isManager;
}

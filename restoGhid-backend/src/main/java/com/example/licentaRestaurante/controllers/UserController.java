package com.example.licentaRestaurante.controllers;

import com.example.licentaRestaurante.entities.LoginRequest;
import com.example.licentaRestaurante.entities.LoginResponse;
import com.example.licentaRestaurante.entities.User;
import com.example.licentaRestaurante.models.bookings.DeletedBooking;
import com.example.licentaRestaurante.models.users.AllUsers;
import com.example.licentaRestaurante.models.users.JwtResponse;
import com.example.licentaRestaurante.models.users.UserResponse;
import com.example.licentaRestaurante.services.UserService;
import com.example.licentaRestaurante.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService usersService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserService usersService, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.usersService = usersService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @GetMapping
    public AllUsers getAllUsers() {
        return usersService.getAllUsers();
    }

    @PostMapping("/register")
    public UserResponse registerUser(@RequestBody User user) {
        return usersService.addUser(user);
    }

    @PostMapping("/login")
    public JwtResponse loginUser(@RequestBody LoginRequest userData) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userData.getUsername(), userData.getPassword())
            );
        } catch (Exception e) {
            return new JwtResponse(null, "Wrong username or password", 400);
        }

        var token = jwtUtils.generateToken(userData.getUsername());
        var isManager = usersService.isUserManager(userData.getUsername());

        return new JwtResponse(new LoginResponse(token, isManager), "", 200);
    }

    @GetMapping("/data")
    public UserResponse getUserData(@RequestHeader("Authorization") String token) {
        var username = jwtUtils.getUsernameFromToken(token.substring(7));

        return usersService.getUserData(username);
    }

    @DeleteMapping("/bookings/{id}")
    public DeletedBooking deleteBooking(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) {
        var username = jwtUtils.getUsernameFromToken(token.substring(7));
        usersService.deleteBooking(id, username);

        return new DeletedBooking("Deleted", "", 200);
    }
}

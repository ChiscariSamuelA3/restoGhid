package com.example.licentaRestaurante.services;

import com.example.licentaRestaurante.entities.Role;
import com.example.licentaRestaurante.entities.User;
import com.example.licentaRestaurante.models.users.AllUsers;
import com.example.licentaRestaurante.models.users.UserResponse;
import com.example.licentaRestaurante.repositories.BookingRepository;
import com.example.licentaRestaurante.repositories.RestaurantRepository;
import com.example.licentaRestaurante.repositories.RoleRepository;
import com.example.licentaRestaurante.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BookingRepository bookingRepository;
    private final RestaurantRepository restaurantRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository, BookingRepository bookingRepository, RestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.bookingRepository = bookingRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public AllUsers getAllUsers() {
        var users = userRepository.findAll();

        if (users.isEmpty()) {
            return new AllUsers(new ArrayList<>(), "No users found", 404);
        }

        return new AllUsers(users, "", 200);
    }

    public Boolean isUserManager(String username) {
        var user = userRepository.findByUsername(username);
        var role = roleRepository.findByName("MANAGER");

        return user.getRoles().contains(role);
    }

    public UserResponse getUserById(Long id) {
        var user = userRepository.findById(id);

        if (user.isEmpty()) {
            return new UserResponse(null, "User not found", 404);
        }

        return new UserResponse(user.get(), "", 200);
    }

    public UserResponse getUserByUsername(String username) {
        var user = userRepository.findByUsername(username);

        if (user == null) {
            return new UserResponse(null, "User not found", 404);
        }

        return new UserResponse(user, "", 200);
    }

    public UserResponse addUser(User user) {
        var dbUser = userRepository.findByUsername(user.getUsername());

        if (dbUser != null) {
            return new UserResponse(null, "User already exists", 400);
        }

        dbUser = userRepository.findByEmail(user.getEmail());

        if (dbUser != null) {
            return new UserResponse(null, "Email already exists", 400);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var restaurants = restaurantRepository.findAll();

        // add role to user's roles list
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("USER"));

        for (var restaurant : restaurants) {
            if (restaurant.getEmail() == null) {
                continue;
            }
            if (restaurant.getEmail().equals(user.getEmail())) {
                roles.add(roleRepository.findByName("MANAGER"));
                restaurant.setManagerUsername(user.getUsername());
                restaurantRepository.save(restaurant);

                break;
            }
        }

        user.setRoles(roles);

        userRepository.save(user);

        return new UserResponse(user, "", 200);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username);

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        }

        throw new UsernameNotFoundException("User not found");
    }

    public UserResponse getUserData(String username) {
        var user = userRepository.findByUsername(username);

        if (user == null) {
            return new UserResponse(null, "User not found", 404);
        }

        return new UserResponse(user, "", 200);
    }

    public void deleteBooking(Long id, String username) {
        var booking = bookingRepository.findById(id);

        if (booking.isEmpty()) {
            return;
        }

        // remove booking from user's bookings list
        var user = userRepository.findByUsername(username);
        user.removeBooking(booking.get());

        // remove booking from database
        bookingRepository.deleteById(id);

        // save user
        userRepository.save(user);
    }
}

package com.example.licentaRestaurante.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "review")
@AllArgsConstructor
@RequiredArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "score")
    private Double score;

    @Column(name = "content")
    private String content;

    @Column(name = "reply")
    private String reply;

    @Column(name = "username")
    private String username;

    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "id_restaurant")
    private String idRestaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", insertable = false, updatable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_restaurant", insertable = false, updatable = false)
    @JsonIgnore
    private Restaurant restaurant;
}

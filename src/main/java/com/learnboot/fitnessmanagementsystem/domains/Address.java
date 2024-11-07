package com.learnboot.fitnessmanagementsystem.domains;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

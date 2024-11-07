package com.learnboot.fitnessmanagementsystem.domains;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String feedback;
    private int stars;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private User trainer;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User Student;

}

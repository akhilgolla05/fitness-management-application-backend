package com.learnboot.fitnessmanagementsystem.domains;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "trainer_id")
//@Builder
public class Trainer extends User{

    private long id;
    private String specialization;

}

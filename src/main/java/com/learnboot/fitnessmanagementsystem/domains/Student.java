package com.learnboot.fitnessmanagementsystem.domains;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "student_id")
//@Builder
public class Student extends User {

    private long id;
}

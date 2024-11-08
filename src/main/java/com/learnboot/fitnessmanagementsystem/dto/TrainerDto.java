package com.learnboot.fitnessmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainerDto {

    private long id;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String email;
    private String phoneNumber;
    private String specialization;
}

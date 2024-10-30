package com.learnboot.fitnessmanagementsystem.request;

import com.learnboot.fitnessmanagementsystem.dto.AddressDto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateRequest {

    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String phoneNumber;
    private LocalDate birthDate;
    private String specialization;

}

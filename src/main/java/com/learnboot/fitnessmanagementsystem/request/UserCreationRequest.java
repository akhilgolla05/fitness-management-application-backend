package com.learnboot.fitnessmanagementsystem.request;

import com.learnboot.fitnessmanagementsystem.dto.AddressDto;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Data
public class UserCreationRequest {

    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String username;
    private String password;
    private String userType;
    private String email;
    private String phoneNumber;
    private boolean isActive;
    private LocalDate birthDate;
    private String specialization;

    private AddressDto address;
}

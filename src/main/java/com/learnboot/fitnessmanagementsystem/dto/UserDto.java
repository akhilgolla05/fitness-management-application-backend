package com.learnboot.fitnessmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.learnboot.fitnessmanagementsystem.domains.Address;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDto {

    private long id;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String username;
    private String userType;
    private String email;
    private String phoneNumber;
    private boolean isActive;
    private LocalDate birthDate;
    private LocalDateTime createdBy;
    private String specialization;
    private List<AddressDto> address;
}

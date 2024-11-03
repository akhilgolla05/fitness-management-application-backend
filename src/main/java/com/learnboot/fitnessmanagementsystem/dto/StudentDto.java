package com.learnboot.fitnessmanagementsystem.dto;

import lombok.Data;

@Data
public class StudentDto {

    private long id;
    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String email;
    private String phoneNumber;
}

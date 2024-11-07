package com.learnboot.fitnessmanagementsystem.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddressDto {

    private long id;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;
}

package com.learnboot.fitnessmanagementsystem.request;

import lombok.Data;

@Data
public class ReviewCreateRequest {

    private String feedback;
    private int stars;

}

package com.learnboot.fitnessmanagementsystem.exceptions;

import lombok.Data;

@Data
public class ErrorMessage {

    private String message;
    private int statusCode;
    private long timestamp;

}

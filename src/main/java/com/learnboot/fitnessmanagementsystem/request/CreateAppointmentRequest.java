package com.learnboot.fitnessmanagementsystem.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateAppointmentRequest {

    private String appointmentDate;
    private String appointmentTime;
    private String reason;
}

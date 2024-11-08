package com.learnboot.fitnessmanagementsystem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.learnboot.fitnessmanagementsystem.domains.AppointmentStatus;
import com.learnboot.fitnessmanagementsystem.domains.User;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDto {

    private long id;
    private String reason;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String appointmentNumber;
    private LocalDateTime createdAt;
    private AppointmentStatus appointmentStatus;
    private StudentDto student;
    private TrainerDto trainer;

}

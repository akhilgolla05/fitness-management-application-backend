package com.learnboot.fitnessmanagementsystem.domains;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.processing.Pattern;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;
import java.util.random.RandomGenerator;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
//        name="Appointment",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"appointment_number"})
)
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String reason;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate appointmentDate;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime appointmentTime;
    private String appointmentNumber;

    @CreationTimestamp
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    @ManyToOne
    private User student;
    @ManyToOne
    private User trainer;


    public void setAppointmentNumber() {
        StringBuilder randomNumber = new StringBuilder();
        Random rand = new Random();
        for(int i=0;i<10;i++){
            randomNumber.append(rand.nextInt(0,10));
        }
        this.appointmentNumber = randomNumber.toString();
    }
}

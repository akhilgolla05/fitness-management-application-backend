package com.learnboot.fitnessmanagementsystem.repository;

import com.learnboot.fitnessmanagementsystem.domains.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByAppointmentNumber(String appointmentNumber);

    @Query("select a from Appointment a where a.sender.id=:userId or a.receiver.id=:userId")
    List<Appointment> findAllByUserId(long userId);
}

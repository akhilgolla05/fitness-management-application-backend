package com.learnboot.fitnessmanagementsystem.repository;

import com.learnboot.fitnessmanagementsystem.domains.Appointment;
import com.learnboot.fitnessmanagementsystem.domains.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByAppointmentNumber(String appointmentNumber);

    @Query("select a from Appointment a where a.student.id=:userId or a.trainer.id=:userId")
    List<Appointment> findAllByUserId(long userId);

    Optional<List<Appointment>> findByStudentIdAndTrainerId(long studentId, long trainerId);

    boolean existsByStudentIdAndTrainerIdAndAppointmentStatus(long studentId, long trainerId, AppointmentStatus appointmentStatus);

    @Query("select a from Appointment a where a.student.id = :studentId and a.trainer.id = :trainerId and a.appointmentStatus = :appointmentStatus")
    Appointment findByStudentIdAndTrainerIdWithStatusCompleted(long studentId, long trainerId, AppointmentStatus appointmentStatus);
}

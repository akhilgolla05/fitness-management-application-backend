package com.learnboot.fitnessmanagementsystem.controller;

import com.learnboot.fitnessmanagementsystem.dto.AppointmentDto;
import com.learnboot.fitnessmanagementsystem.request.CreateAppointmentRequest;
import com.learnboot.fitnessmanagementsystem.response.ApiResponse;
import com.learnboot.fitnessmanagementsystem.service.appointment.IAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final IAppointmentService appointmentService;

    @PostMapping("/create-appointment")
    public ResponseEntity<ApiResponse> createAppointment(@RequestBody CreateAppointmentRequest appointment,
                                                         @RequestParam long senderId, @RequestParam long receiverId) {
        AppointmentDto appointmentDto = appointmentService.createAppointment(appointment, senderId, receiverId);
        return ResponseEntity.ok(new ApiResponse("Appointment Created Successfully", appointmentDto));

    }

    @GetMapping("/get-appointment/{appointmentId}")
    public ResponseEntity<ApiResponse> getAppointmentById(@PathVariable long appointmentId) {
        AppointmentDto appointmentDto = appointmentService.getAppointment(appointmentId);
        return ResponseEntity.ok(new ApiResponse("Appointment Found Successfully", appointmentDto));
    }

    @PutMapping("/update-appointment/{appointmentId}")
    public ResponseEntity<ApiResponse> updateAppointment(@RequestBody CreateAppointmentRequest appointment,
                                                         @PathVariable long appointmentId) {
        AppointmentDto appointmentDto = appointmentService.updateAppointment(appointmentId, appointment);
        return ResponseEntity.ok(new ApiResponse("Appointment Updated Successfully", appointmentDto));

    }

    @DeleteMapping("/delete-appointment/{appointmentId}")
    public ResponseEntity<ApiResponse> deleteAppointment(@PathVariable long appointmentId) {
        appointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.ok(new ApiResponse("Appointment Deleted Successfully",null));
    }

    @GetMapping("/get-appointment-by-appointmentNumber/{appointmentNumber}")
    public ResponseEntity<ApiResponse> getAppointmentById(@PathVariable String appointmentNumber) {
        AppointmentDto appointmentDto = appointmentService.getAppointmentByAppointmentNumber(appointmentNumber);
        return ResponseEntity.ok(new ApiResponse("Appointment Found Successfully", appointmentDto));
    }

    @GetMapping("/get-appointments-for-user/{userId}")
    public ResponseEntity<ApiResponse> getAppointmentForUser(@PathVariable long userId) {
        List<AppointmentDto> appointmentDtos = appointmentService.getAllAppointmentsForAUser(userId);
        return ResponseEntity.ok(new ApiResponse("Appointment Found Successfully", appointmentDtos));
    }

}

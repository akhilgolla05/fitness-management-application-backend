package com.learnboot.fitnessmanagementsystem.service.appointment;

import com.learnboot.fitnessmanagementsystem.dto.AppointmentDto;
import com.learnboot.fitnessmanagementsystem.request.CreateAppointmentRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IAppointmentService {
    AppointmentDto createAppointment(CreateAppointmentRequest request, long senderId, long recepientId);

    AppointmentDto getAppointment(long appointmentId);

    AppointmentDto getAppointmentByAppointmentNumber(String appointmentNumber);

    void deleteAppointment(long appointmentId);

    List<AppointmentDto> getAllAppointmentsForAUser(long userId);

    AppointmentDto updateAppointment(@PathVariable long appointmentId,
                                     @RequestBody CreateAppointmentRequest request);
}

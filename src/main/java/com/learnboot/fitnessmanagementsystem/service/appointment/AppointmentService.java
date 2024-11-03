package com.learnboot.fitnessmanagementsystem.service.appointment;

import com.learnboot.fitnessmanagementsystem.domains.Appointment;
import com.learnboot.fitnessmanagementsystem.domains.AppointmentStatus;
import com.learnboot.fitnessmanagementsystem.domains.User;
import com.learnboot.fitnessmanagementsystem.dto.AppointmentDto;
import com.learnboot.fitnessmanagementsystem.dto.StudentDto;
import com.learnboot.fitnessmanagementsystem.dto.TrainerDto;
import com.learnboot.fitnessmanagementsystem.exceptions.ResourceNotFoundException;
import com.learnboot.fitnessmanagementsystem.repository.AppointmentRepository;
import com.learnboot.fitnessmanagementsystem.repository.UserRepository;
import com.learnboot.fitnessmanagementsystem.request.CreateAppointmentRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService implements IAppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public AppointmentDto createAppointment(CreateAppointmentRequest request, long senderId, long recepientId) {

        User student = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Optional<User> trainer = userRepository.findById(recepientId);
        boolean isTrainer = false;
        if(trainer.isPresent()) {
            isTrainer = trainer.get().getUserType().equals("TRAINER") ;
        }else{
            throw new ResourceNotFoundException("Trainer Not Found");
        }
        if(isTrainer) {
            Appointment appointment = new Appointment();
            appointment.setAppointmentDate(LocalDate.parse(request.getAppointmentDate()));
            appointment.setAppointmentTime(LocalTime.parse(request.getAppointmentTime()));
            appointment.setReason(request.getReason());
            appointment.setAppointmentNumber();
            appointment.setSender(student);
            appointment.setReceiver(trainer.get());
            appointment.setAppointmentStatus(AppointmentStatus.PENDING);
            Appointment dbAppointment =  appointmentRepository.save(appointment);
            return mapAppointmentToDto(dbAppointment);
        }else{
            throw new IllegalArgumentException("Please Select the Trainer");
        }
    }

    @Override
    public AppointmentDto getAppointment(long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment Not Found"));
        return mapAppointmentToDto(appointment);
    }



    @Override
    public AppointmentDto getAppointmentByAppointmentNumber(String appointmentNumber) {
        Appointment appointment = appointmentRepository.findByAppointmentNumber(appointmentNumber)
                .orElseThrow(()-> new ResourceNotFoundException("Appointment Not Found!"));
        return mapAppointmentToDto(appointment);
    }

    @Override
    public void deleteAppointment(long appointmentId) {
        appointmentRepository.findById(appointmentId)
                .ifPresentOrElse(appointmentRepository::delete, () -> {
                    throw new ResourceNotFoundException("Appointment Not Found");
                });
    }

        @Override
        public List<AppointmentDto> getAllAppointmentsForAUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Appointment> appointments = appointmentRepository.findAllByUserId(userId);
        return appointments.stream()
                .map(this::mapAppointmentToDto)
                .toList();
    }

    @Override
    public AppointmentDto updateAppointment( long appointmentId,CreateAppointmentRequest request){
        return appointmentRepository.findById(appointmentId)
                .map(appointment -> {
                    if(Objects.equals(appointment.getAppointmentStatus(),AppointmentStatus.PENDING)){
                        appointment.setAppointmentTime(LocalTime.parse(request.getAppointmentTime()));
                        appointment.setReason(request.getReason());
                        appointment.setAppointmentTime(LocalTime.parse(request.getAppointmentTime()));
                        Appointment updatedAppointment = appointmentRepository.save(appointment);
                        return mapAppointmentToDto(updatedAppointment);
                    }else{
                        throw new IllegalArgumentException("Appointment Cannot be Updated");
                    }
                }).orElseThrow(() -> new ResourceNotFoundException("Appointment Not Found"));
    }

    private AppointmentDto mapAppointmentToDto(Appointment appointment) {
        AppointmentDto appointmentDto = modelMapper.map(appointment, AppointmentDto.class);
        User student= appointment.getSender();
        appointmentDto.setStudent(modelMapper.map(student, StudentDto.class));
        User trainer = appointment.getReceiver();
        appointmentDto.setTrainer(modelMapper.map(trainer, TrainerDto.class));
        return appointmentDto;
    }

}

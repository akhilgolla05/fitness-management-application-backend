package com.learnboot.fitnessmanagementsystem.service.appointment;

import com.learnboot.fitnessmanagementsystem.domains.*;
import com.learnboot.fitnessmanagementsystem.dto.AppointmentDto;
import com.learnboot.fitnessmanagementsystem.dto.StudentDto;
import com.learnboot.fitnessmanagementsystem.dto.TrainerDto;
import com.learnboot.fitnessmanagementsystem.exceptions.ResourceNotFoundException;
import com.learnboot.fitnessmanagementsystem.repository.AppointmentRepository;
import com.learnboot.fitnessmanagementsystem.repository.UserRepository;
import com.learnboot.fitnessmanagementsystem.request.CreateAppointmentRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment appointment;
    private User student;
    private User trainer;
    private AppointmentDto appointmentDto;
    private CreateAppointmentRequest request;
    @BeforeEach
    public void BeforeEach(){
         request = CreateAppointmentRequest.builder()
                 .appointmentDate("2024-11-07")
                 //.appointmentTime(String.valueOf(LocalTime.now().getHour()))
                 .appointmentTime("10:00")
                 .reason("consultation").build();
        student = User.builder()
                .id(1L).firstName("munna")
                .lastName("bhai").email("munna@gmail.com")
                .userType("STUDENT").username("munna123").build();
        trainer = User.builder()
                .id(2L).firstName("suresh")
                .lastName("raj").email("suresh@gmail.com")
                .specialization("cardio")
                .userType("TRAINER").username("suresh123").build();
        appointment = Appointment.builder()
                .id(1L).appointmentDate(LocalDate.parse(request.getAppointmentDate()))
                .appointmentTime(LocalTime.parse(request.getAppointmentTime())).reason(request.getReason())
                .appointmentStatus(AppointmentStatus.PENDING).appointmentNumber("23456")
                .student(student).trainer(trainer).build();

        StudentDto studentDto =  StudentDto.builder()
                .id(1L).firstName("munna")
                .lastName("bhai").email("munna@gmail.com")
                .build();

        TrainerDto trainerDto = TrainerDto.builder()
                .id(2L).firstName("suresh")
                .lastName("raj").email("suresh@gmail.com").build();

        appointmentDto = AppointmentDto.builder()
                .id(1L).appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime()).reason("reason")
                .appointmentStatus(AppointmentStatus.PENDING).appointmentNumber("23456")
                .student(studentDto).trainer(trainerDto).build();

    }

    @Test
    public void givenAppointment_WhenSave_returnAppointmentDto(){

        BDDMockito.given(userRepository.findById(1L))
                .willReturn(Optional.ofNullable(student));
        BDDMockito.given(userRepository.findById(2L))
                .willReturn(Optional.ofNullable(trainer));
        BDDMockito.given(appointmentRepository.save(ArgumentMatchers.any(Appointment.class)))
                .willReturn(appointment);
        BDDMockito.given(modelMapper.map(appointment, AppointmentDto.class))
                .willReturn(appointmentDto);

        AppointmentDto appointmentDto1 =
                appointmentService.createAppointment(request,student.getId(),trainer.getId());

        assertThat(appointmentDto1).isNotNull();
        assertThat(appointmentDto1.getAppointmentDate()).isEqualTo(appointment.getAppointmentDate());
        assertThat(appointmentDto1.getAppointmentTime()).isEqualTo(appointment.getAppointmentTime());
        assertThat(appointmentDto1.getReason()).isEqualTo(appointment.getReason());
    }

    @Test
    public void givenStudentId_whenFindById_returnThrown(){

        BDDMockito.given(userRepository.findById(1L))
                .willReturn(Optional.empty());
//        BDDMockito.given(userRepository.findById(2L))
//                .willReturn(Optional.ofNullable(trainer));

       assertThrows(ResourceNotFoundException.class,
               ()-> appointmentService.createAppointment(request,1L,trainer.getId()));
    }

    @Test
    public void givenId_whenFindById_returnAppointment(){

        BDDMockito.given(appointmentRepository.findById(appointment.getId()))
                .willReturn(Optional.ofNullable(appointment));
        BDDMockito.given(modelMapper.map(appointment, AppointmentDto.class))
                .willReturn(appointmentDto);

        AppointmentDto dto = appointmentService.getAppointment(appointment.getId());
        assertThat(dto).isNotNull();
        assertThat(dto.getAppointmentDate()).isEqualTo(appointment.getAppointmentDate());
        assertThat(dto.getAppointmentTime()).isEqualTo(appointment.getAppointmentTime());
        assertThat(dto.getReason()).isEqualTo(appointment.getReason());
    }

    @Test
    public void givenAppointmentNumber_whenFindByNumber_returnAppointment(){

        BDDMockito.given(appointmentRepository.findByAppointmentNumber(appointment.getAppointmentNumber()))
                        .willReturn(Optional.ofNullable(appointment));
        BDDMockito.given(modelMapper.map(appointment, AppointmentDto.class))
                .willReturn(appointmentDto);

        AppointmentDto dto = appointmentService.getAppointmentByAppointmentNumber(appointment.getAppointmentNumber());
        assertThat(dto).isNotNull();
        assertThat(dto.getAppointmentDate()).isEqualTo(appointment.getAppointmentDate());
        assertThat(dto.getAppointmentTime()).isEqualTo(appointment.getAppointmentTime());
        assertThat(dto.getReason()).isEqualTo(appointment.getReason());
    }

    @Test
    public void givenAppointmentId_whenDelete_returnNothing(){

        BDDMockito.given(appointmentRepository.findById(appointment.getId())).willReturn(Optional.of(appointment));
        BDDMockito.doNothing().when(appointmentRepository).delete(ArgumentMatchers.any(Appointment.class));

        appointmentService.deleteAppointment(appointment.getId());

        verify(appointmentRepository,times(1))
                .delete(ArgumentMatchers.any(Appointment.class));

    }

    @Test
    public void givenUserId_whenFindAll_returnAppointments(){

        User trainer2 = User.builder()
                .id(3L).firstName("naveen")
                .lastName("vanga").email("naveen@gmail.com")
                .specialization("yoga")
                .userType("TRAINER").username("naveen123").build();

        CreateAppointmentRequest request2 = CreateAppointmentRequest.builder()
                .appointmentDate("2024-11-07")
                //.appointmentTime(String.valueOf(LocalTime.now().getHour()))
                .appointmentTime("10:00")
                .reason("meet the doctor").build();

        Appointment appointment2 = appointment = Appointment.builder()
                .id(1L).appointmentDate(LocalDate.parse(request2.getAppointmentDate()))
                .appointmentTime(LocalTime.parse(request2.getAppointmentTime())).reason(request2.getReason())
                .appointmentStatus(AppointmentStatus.PENDING).appointmentNumber("11111")
                .student(student).trainer(trainer2).build();

//        StudentDto studentDto =  StudentDto.builder()
//                .id(student.getId()).firstName(student.getFirstName())
//                .lastName(student.getLastName()).email(student.getEmail())
//                .build();

//        TrainerDto trainerDto2 = TrainerDto.builder()
//                .id(trainer2.getId()).firstName(trainer2.getFirstName())
//                .lastName(trainer2.getLastName()).email(trainer2.getEmail())
//                .specialization(trainer2.getSpecialization()).build();

//        AppointmentDto appointmentDto2 = AppointmentDto.builder()
//                .id(appointment2.getId()).appointmentDate(appointment2.getAppointmentDate())
//                .appointmentTime(appointment2.getAppointmentTime()).reason(appointment2.getReason())
//                .appointmentStatus(appointment2.getAppointmentStatus())
//                .appointmentNumber(appointment2.getAppointmentNumber())
//                .student(studentDto).trainer(trainerDto2).build();


        ArrayList<Appointment> appointmentList = new ArrayList<>();
        appointmentList.add(appointment2);
        appointmentList.add(appointment);

//        ArrayList<AppointmentDto> appointmentDtoList = new ArrayList<>();
//        appointmentDtoList.add(appointmentDto2);
//        appointmentDtoList.add(appointmentDto);

        BDDMockito.given(userRepository.findById(1L))
                .willReturn(Optional.of(student));

        BDDMockito.given(appointmentRepository.findAllByUserId(student.getId()))
                .willReturn(appointmentList);

        lenient().when(modelMapper.map(ArgumentMatchers.any(Appointment.class),ArgumentMatchers.eq(AppointmentDto.class)))
                .thenAnswer(invocation->{
                    Appointment app = invocation.getArgument(0);

                    StudentDto studentDto = StudentDto.builder()
                            .id(app.getStudent().getId())
                            .firstName(app.getStudent().getFirstName())
                            .lastName(app.getStudent().getLastName())
                            .email(app.getStudent().getEmail())
                            .build();

                    TrainerDto trainerDto = TrainerDto.builder()
                            .id(app.getStudent().getId())
                            .firstName(app.getStudent().getFirstName())
                            .lastName(app.getStudent().getLastName())
                            .email(app.getStudent().getEmail())
                            .build();

//                    List<TrainerDto> trainerDtos = app.getTrainer().stream()
//                            .map(trainer -> TrainerDto.builder()
//                                    .id(trainer.getId())
//                                    .firstName(trainer.getFirstName())
//                                    .lastName(trainer.getLastName())
//                                    .email(trainer.getEmail())
//                                    .specialization(trainer.getSpecialization())
//                                    .build())
//                            .collect(Collectors.toList());

                    return AppointmentDto.builder()
                            .id(app.getId())
                            .appointmentDate(app.getAppointmentDate())
                            .appointmentTime(app.getAppointmentTime())
                            .reason(app.getReason())
                            .appointmentStatus(app.getAppointmentStatus())
                            .appointmentNumber(app.getAppointmentNumber())
                            .student(studentDto)
                            .trainer(trainerDto)
                            .build();
                });

      List<AppointmentDto> dtos = appointmentService.getAllAppointmentsForAUser(student.getId());

      assertThat(dtos.size()).isEqualTo(2);
      assertThat(dtos.get(0).getAppointmentDate()).isEqualTo(appointment.getAppointmentDate());

    }


//    @Test
//    public void givenUserId_whenFindAllForUser_returnAppointments() {
//        User trainer1 = User.builder()
//                .id(2L).firstName("john")
//                .lastName("doe").email("john@gmail.com")
//                .specialization("pilates")
//                .userType("TRAINER").username("john123").build();
//
//        User trainer2 = User.builder()
//                .id(3L).firstName("naveen")
//                .lastName("vanga").email("naveen@gmail.com")
//                .specialization("yoga")
//                .userType("TRAINER").username("naveen123").build();
//
//        CreateAppointmentRequest request2 = CreateAppointmentRequest.builder()
//                .appointmentDate("2024-11-07")
//                .appointmentTime("10:00")
//                .reason("meet the doctor").build();
//
//        Appointment appointment2 = Appointment.builder()
//                .id(1L).appointmentDate(LocalDate.parse(request2.getAppointmentDate()))
//                .appointmentTime(LocalTime.parse(request2.getAppointmentTime())).reason(request2.getReason())
//                .appointmentStatus(AppointmentStatus.PENDING).appointmentNumber("11111")
//                .student(student).trainers(Arrays.asList(trainer1, trainer2)).build();
//
//        ArrayList<Appointment> appointmentList = new ArrayList<>();
//        appointmentList.add(appointment2);
//        appointmentList.add(appointment);
//
//        BDDMockito.given(userRepository.findById(1L))
//                .willReturn(Optional.of(student));
//
//        BDDMockito.given(appointmentRepository.findAllByUserId(student.getId()))
//                .willReturn(appointmentList);
//
//        lenient().when(modelMapper.map(ArgumentMatchers.any(Appointment.class), ArgumentMatchers.eq(AppointmentDto.class)))
//                .thenAnswer(invocation -> {
//                    Appointment app = invocation.getArgument(0);
//
//                    StudentDto studentDto = StudentDto.builder()
//                            .id(app.getStudent().getId())
//                            .firstName(app.getStudent().getFirstName())
//                            .lastName(app.getStudent().getLastName())
//                            .email(app.getStudent().getEmail())
//                            .build();
//
//                    List<TrainerDto> trainerDtos = app.getTrainers().stream()
//                            .map(trainer -> TrainerDto.builder()
//                                    .id(trainer.getId())
//                                    .firstName(trainer.getFirstName())
//                                    .lastName(trainer.getLastName())
//                                    .email(trainer.getEmail())
//                                    .specialization(trainer.getSpecialization())
//                                    .build())
//                            .collect(Collectors.toList());
//
//                    return AppointmentDto.builder()
//                            .id(app.getId())
//                            .appointmentDate(app.getAppointmentDate())
//                            .appointmentTime(app.getAppointmentTime())
//                            .reason(app.getReason())
//                            .appointmentStatus(app.getAppointmentStatus())
//                            .appointmentNumber(app.getAppointmentNumber())
//                            .student(studentDto)
//                            .trainers(trainerDtos)
//                            .build();
//                });
//
//        List<AppointmentDto> dtos = appointmentService.getAllAppointmentsForAUser(student.getId());
//
//        assertThat(dtos.size()).isEqualTo(2);
//        assertThat(dtos.get(0).getAppointmentDate()).isEqualTo(appointment.getAppointmentDate());
//        assertThat(dtos.get(0).getTrainers().size()).isEqualTo(2); // Check if trainers list has two trainers
//    }



}
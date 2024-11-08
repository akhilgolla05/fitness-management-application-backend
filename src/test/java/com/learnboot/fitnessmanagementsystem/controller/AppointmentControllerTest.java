package com.learnboot.fitnessmanagementsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnboot.fitnessmanagementsystem.domains.Appointment;
import com.learnboot.fitnessmanagementsystem.domains.AppointmentStatus;
import com.learnboot.fitnessmanagementsystem.domains.User;
import com.learnboot.fitnessmanagementsystem.dto.AppointmentDto;
import com.learnboot.fitnessmanagementsystem.dto.StudentDto;
import com.learnboot.fitnessmanagementsystem.dto.TrainerDto;
import com.learnboot.fitnessmanagementsystem.exceptions.ResourceNotFoundException;
import com.learnboot.fitnessmanagementsystem.request.CreateAppointmentRequest;
import com.learnboot.fitnessmanagementsystem.service.appointment.IAppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AppointmentController.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Appointment appointment;
    private User student;
    private User trainer;
    private AppointmentDto appointmentDto;
    private CreateAppointmentRequest request;
    private StudentDto studentDto;
    private TrainerDto trainerDto;
    @BeforeEach
    public void setUp(){
        request = CreateAppointmentRequest.builder()
                .appointmentDate("2024-11-07")
                //.appointmentTime(String.valueOf(LocalTime.now().getHour()))
                .appointmentTime("10:00:00")
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

         studentDto =  StudentDto.builder()
                .id(1L).firstName("munna")
                .lastName("bhai").email("munna@gmail.com")
                .build();

         trainerDto = TrainerDto.builder()
                .id(2L).firstName("suresh")
                .lastName("raj").email("suresh@gmail.com").build();

        appointmentDto = AppointmentDto.builder()
                .id(1L).appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime()).reason(appointment.getReason())
                .appointmentStatus(appointment.getAppointmentStatus()).appointmentNumber(appointment.getAppointmentNumber())
                .student(studentDto).trainer(trainerDto).build();

    }

    @Test
    public void givenAppointmentRequest_whenCreated_returnAppointment() throws Exception {

        BDDMockito.given(appointmentService.
                        createAppointment(ArgumentMatchers.any(CreateAppointmentRequest.class),
                                ArgumentMatchers.eq(1L),ArgumentMatchers.eq(2L)))
                .willReturn(appointmentDto);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/appointments/create-appointment")
                        .param("studentId","1")
                        .param("trainerId","2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.appointmentDate").value(request.getAppointmentDate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.appointmentTime").value(request.getAppointmentTime()));

    }

    @Test
    public void givenAppointmentId_whenFindById_returnAppointment() throws Exception {

        BDDMockito.given(appointmentService.getAppointment(ArgumentMatchers.anyLong()))
                .willReturn(appointmentDto);

        ResultActions actions =
                mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/appointments/get-appointment/{appointmentId}" ,appointment.getId()));

        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.appointmentDate").value(request.getAppointmentDate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.appointmentTime").value(request.getAppointmentTime()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.reason").value(request.getReason()));

    }

    @Test
    public void givenAppointmentId_whenFindById_returnException() throws Exception {

        BDDMockito.given(appointmentService.getAppointment(1L))
                .willReturn(appointmentDto);

        BDDMockito.given(appointmentService.getAppointment(2L))
                .willThrow(ResourceNotFoundException.class);

//        ResultActions actions =
//                mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/appointments/get-appointment/{appointmentId}" ,appointment.getId()));

        ResultActions actions2 =
                mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/appointments/get-appointment/{appointmentId}" ,2L));

        //actions.andExpect(MockMvcResultMatchers.status().isOk());
        actions2.andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    public void givenAppointmentRequest_whenUpdate_returnAppointment() throws Exception {

        CreateAppointmentRequest updatedRequest = CreateAppointmentRequest.builder()
                .appointmentDate("2024-11-10")
                .appointmentTime("12:00:00")
                .reason("meet the doctor").build();

        appointment = Appointment.builder()
                .id(1L).appointmentDate(LocalDate.parse(updatedRequest.getAppointmentDate()))
                .appointmentTime(LocalTime.parse(updatedRequest.getAppointmentTime()))
                .reason(updatedRequest.getReason())
                .appointmentStatus(AppointmentStatus.PENDING).appointmentNumber("23456")
                .student(student).trainer(trainer).build();

        appointmentDto = AppointmentDto.builder()
                .id(1L).appointmentDate(appointment.getAppointmentDate())
                .appointmentTime(appointment.getAppointmentTime()).reason(appointment.getReason())
                .appointmentStatus(appointment.getAppointmentStatus()).appointmentNumber(appointment.getAppointmentNumber())
                .student(studentDto).trainer(trainerDto).build();

        BDDMockito.given(appointmentService.updateAppointment(ArgumentMatchers.eq(1L), ArgumentMatchers.any(CreateAppointmentRequest.class)))
                .willReturn(appointmentDto);

       ResultActions actions =  mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/appointments/update-appointment/{appointmentId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedRequest)));

       actions.andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.jsonPath("$.data.appointmentDate").value(updatedRequest.getAppointmentDate()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.data.appointmentTime").value(updatedRequest.getAppointmentTime()))
               .andExpect(MockMvcResultMatchers.jsonPath("$.data.reason").value(updatedRequest.getReason()));

    }

    @Test
    public void givenAppointmentId_whenDelete_returnNothing() throws Exception {

        BDDMockito.doNothing().when(appointmentService).deleteAppointment(ArgumentMatchers.anyLong());

        ResultActions actions =
                mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/appointments/delete-appointment/{appointmentId}",1L));

        actions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Appointment Deleted Successfully"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void givenAppointmentId_whenDelete_returnException() throws Exception {

        BDDMockito.doThrow(ResourceNotFoundException.class)
                .when(appointmentService).deleteAppointment(3L);

        ResultActions actions =
                mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/appointments/delete-appointment/{appointmentId}",3L));

        actions.andExpect(MockMvcResultMatchers.status().isNotFound());

    }



}
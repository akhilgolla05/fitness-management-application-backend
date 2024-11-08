package com.learnboot.fitnessmanagementsystem.controller;

import com.learnboot.fitnessmanagementsystem.dto.TrainerDto;
import com.learnboot.fitnessmanagementsystem.response.ApiResponse;
import com.learnboot.fitnessmanagementsystem.service.trainer.ITrainerService;
import com.learnboot.fitnessmanagementsystem.service.trainer.TrainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/trainers")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3302")
public class TrainerController {

    private final ITrainerService trainerService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllTrainers() {
        List<TrainerDto> trainerDtoList = trainerService.getAllTrainers();
        return ResponseEntity.ok(new ApiResponse("Trainers Found", trainerDtoList));
    }

}

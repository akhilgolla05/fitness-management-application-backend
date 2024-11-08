package com.learnboot.fitnessmanagementsystem.service.trainer;

import com.learnboot.fitnessmanagementsystem.dto.TrainerDto;

import java.util.List;

public interface ITrainerService {
    List<TrainerDto> getAllTrainers();
}

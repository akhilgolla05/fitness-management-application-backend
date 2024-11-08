package com.learnboot.fitnessmanagementsystem.service.trainer;

import com.learnboot.fitnessmanagementsystem.domains.Trainer;
import com.learnboot.fitnessmanagementsystem.dto.TrainerDto;
import com.learnboot.fitnessmanagementsystem.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerService implements ITrainerService {

    private final TrainerRepository trainerRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<TrainerDto> getAllTrainers(){
        List<Trainer> trainers =  trainerRepository.findAll();

        return trainers.stream()
                .map(trainer -> modelMapper.map(trainer, TrainerDto.class))
                .toList();
    }
}

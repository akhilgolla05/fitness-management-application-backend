package com.learnboot.fitnessmanagementsystem.Utils;

import com.learnboot.fitnessmanagementsystem.exceptions.InvalidUserTypeException;
import com.learnboot.fitnessmanagementsystem.exceptions.UserAlreadyExistsException;
import com.learnboot.fitnessmanagementsystem.domains.Admin;
import com.learnboot.fitnessmanagementsystem.domains.Student;
import com.learnboot.fitnessmanagementsystem.domains.Trainer;
import com.learnboot.fitnessmanagementsystem.domains.User;
import com.learnboot.fitnessmanagementsystem.exceptions.UsernameAlreadyExistsException;
import com.learnboot.fitnessmanagementsystem.repository.AdminRepository;
import com.learnboot.fitnessmanagementsystem.repository.StudentRepository;
import com.learnboot.fitnessmanagementsystem.repository.TrainerRepository;
import com.learnboot.fitnessmanagementsystem.repository.UserRepository;
import com.learnboot.fitnessmanagementsystem.request.UserCreationRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleUserCreation implements UserCreation{

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final StudentRepository studentRepository;
    private final AdminRepository adminRepository;

    @Override
    public User createUser(UserCreationRequest request) {
        if(userRepository.existsByEmail(request.getEmail())){
            throw new UserAlreadyExistsException("User Already Exists");
        }
        if(!userRepository.existsByEmail(request.getEmail()) && userRepository.existsByUsername(request.getUsername())){
            throw new UsernameAlreadyExistsException("User Available with Provided Username, give the Different username");
        }
        String type = request.getUserType();
        switch (type){
            case "STUDENT"->{
                return createStudent(request);
            }
            case "ADMIN"->{
                return createAdmin(request);
            }
            case "TRAINER"->{
                return createTrainer(request);
            }
            default -> throw new InvalidUserTypeException("User Type is Invalid!");
        }
        //return null;
    }

    private User createStudent(UserCreationRequest request){
        Student student = modelMapper.map(request, Student.class);
        return studentRepository.save(student);
    }
    private User createTrainer(UserCreationRequest request){
        Trainer trainer = modelMapper.map(request, Trainer.class);
        return trainerRepository.save(trainer);
    }
    private User createAdmin(UserCreationRequest request){
        Admin admin = modelMapper.map(request, Admin.class);
        return adminRepository.save(admin);
    }
}

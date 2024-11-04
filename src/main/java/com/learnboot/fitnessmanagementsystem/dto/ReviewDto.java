package com.learnboot.fitnessmanagementsystem.dto;

import lombok.Data;

@Data
public class ReviewDto {

   // private int id;
    private String feedback;
    private int stars;
    private long StudentId;
    private String StudentName;
    private long trainerId;
    private String TrainerName;
    private byte[] studentPhoto;
    private byte[] trainerPhoto;

}

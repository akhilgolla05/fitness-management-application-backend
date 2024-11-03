package com.learnboot.fitnessmanagementsystem.dto;

import jakarta.persistence.Lob;
import lombok.Data;

import java.sql.Blob;

@Data
public class PhotoDto {

    private long id;
    private byte[] image;
    private String filename;
    private String fileType;
}

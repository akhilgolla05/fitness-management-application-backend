package com.learnboot.fitnessmanagementsystem.domains;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Lob
    private Blob image;
    private String filename;
    private String fileType;

    @OneToOne(mappedBy = "photo")
    private User user;
}

package com.learnboot.fitnessmanagementsystem.domains;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "admin_id")
//@Builder
public class Admin extends User {

    private long id;
}

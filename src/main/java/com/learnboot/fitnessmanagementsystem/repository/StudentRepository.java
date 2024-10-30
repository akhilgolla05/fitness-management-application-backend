package com.learnboot.fitnessmanagementsystem.repository;

import com.learnboot.fitnessmanagementsystem.domains.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}

package com.learnboot.fitnessmanagementsystem.repository;

import com.learnboot.fitnessmanagementsystem.domains.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
}

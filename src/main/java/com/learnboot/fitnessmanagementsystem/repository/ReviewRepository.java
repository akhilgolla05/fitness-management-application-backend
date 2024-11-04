package com.learnboot.fitnessmanagementsystem.repository;

import com.learnboot.fitnessmanagementsystem.domains.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r where r.trainer.id=:userId")
    Optional<List<Review>> findByUserId(@Param("userId") long userId);

}

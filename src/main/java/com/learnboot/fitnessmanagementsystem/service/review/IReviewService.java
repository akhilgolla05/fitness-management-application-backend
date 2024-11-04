package com.learnboot.fitnessmanagementsystem.service.review;

import com.learnboot.fitnessmanagementsystem.dto.ReviewDto;
import com.learnboot.fitnessmanagementsystem.request.ReviewCreateRequest;

import java.sql.SQLException;
import java.util.List;

public interface IReviewService {
    ReviewDto postReview(ReviewCreateRequest request, long studentId, long trainerId) throws SQLException;

    ReviewDto getReview(long reviewId) throws SQLException;

    List<ReviewDto> getAllReviewsForUser(long userId);

    ReviewDto updateReview(long reviewId, ReviewCreateRequest request) throws SQLException;

    void deleteReview(long reviewId) throws SQLException;

    int averageRatingForTrainer(long trainerId);
}

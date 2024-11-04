package com.learnboot.fitnessmanagementsystem.controller;

import com.learnboot.fitnessmanagementsystem.domains.Review;
import com.learnboot.fitnessmanagementsystem.dto.ReviewDto;
import com.learnboot.fitnessmanagementsystem.request.ReviewCreateRequest;
import com.learnboot.fitnessmanagementsystem.response.ApiResponse;
import com.learnboot.fitnessmanagementsystem.service.review.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final IReviewService reviewService;

    @PostMapping("/post-review")
    public ResponseEntity<ApiResponse> postReview(@RequestBody ReviewCreateRequest request,
                                                  @RequestParam long studentId,
                                                  @RequestParam long trainerId) throws SQLException {
        ReviewDto reviewDto = reviewService.postReview(request,studentId,trainerId);
        return ResponseEntity.ok(new ApiResponse("Review Posted Successfully", reviewDto));
    }

    @GetMapping("/get-review/{reviewId}")
    public ResponseEntity<ApiResponse> getReview(@PathVariable long reviewId) throws SQLException {
        ReviewDto reviewDto = reviewService.getReview(reviewId);
        return ResponseEntity.ok(new ApiResponse("Review Found Successfully", reviewDto));
    }

    @GetMapping("/get-reviews-for-trainer/{trainerId}")
    public ResponseEntity<ApiResponse> getReviewsForTrainer(@PathVariable long trainerId) throws SQLException {
        List<ReviewDto> reviewDto = reviewService.getAllReviewsForUser(trainerId);
        return ResponseEntity.ok(new ApiResponse("Review Found Successfully", reviewDto));
    }

    @DeleteMapping("/delete-review/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(@PathVariable long reviewId) throws SQLException {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok(new ApiResponse("Review Deleted Successfully",null));
    }

    @PutMapping("/update-review/{reviewId}")
    public ResponseEntity<ApiResponse> updateReview(@RequestBody ReviewCreateRequest request,
                                                    @PathVariable long reviewId) throws SQLException {
        ReviewDto reviewDto = reviewService.updateReview(reviewId,request);
        return ResponseEntity.ok(new ApiResponse("Review Updated Successfully",reviewDto));
    }

    @GetMapping("/get-rating-for-trainer/{trainerId}")
    public ResponseEntity<ApiResponse> getAverageRatingForTrainer(@PathVariable long trainerId) throws SQLException {

       double averageRating = reviewService.averageRatingForTrainer(trainerId);
       return ResponseEntity.ok(new ApiResponse("Review Found Successfully", averageRating));
    }


}

package com.learnboot.fitnessmanagementsystem.service.review;

import com.learnboot.fitnessmanagementsystem.domains.Appointment;
import com.learnboot.fitnessmanagementsystem.domains.AppointmentStatus;
import com.learnboot.fitnessmanagementsystem.domains.Review;
import com.learnboot.fitnessmanagementsystem.domains.User;
import com.learnboot.fitnessmanagementsystem.dto.ReviewDto;
import com.learnboot.fitnessmanagementsystem.exceptions.ResourceNotFoundException;
import com.learnboot.fitnessmanagementsystem.repository.AppointmentRepository;
import com.learnboot.fitnessmanagementsystem.repository.ReviewRepository;
import com.learnboot.fitnessmanagementsystem.repository.UserRepository;
import com.learnboot.fitnessmanagementsystem.request.ReviewCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.OptionalDouble;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final AppointmentRepository appointmentRepository;

    @Override
    public ReviewDto postReview(ReviewCreateRequest request, long studentId, long trainerId) throws SQLException {

        if(studentId == trainerId){
            throw new IllegalArgumentException("Student id cannot be the same as trainer id");
        }
        boolean isCompleted =
                appointmentRepository.existsByStudentIdAndTrainerIdAndAppointmentStatus(studentId, trainerId,AppointmentStatus.COMPLETED);
        log.info("appointment exisis : {}", isCompleted);

        if(isCompleted){
            User student = userRepository.findById(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
            User trainer = userRepository.findById(trainerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Trainer Not Found"));
            Review review = new Review();
            review.setFeedback(request.getFeedback());
            review.setStars(request.getStars());
            review.setStudent(student);
            review.setTrainer(trainer);
            review.setFeedback(review.getFeedback());
            review.setStars(review.getStars());
            Review savedReview = reviewRepository.save(review);
            return mapToReviewDto(savedReview);
        }else{
            throw new RuntimeException("You are Not Allowed to Review");
        }
    }

    @Override
    public ReviewDto getReview(long reviewId) throws SQLException {
       Review review =  reviewRepository.findById(reviewId)
                .orElseThrow(()-> new ResourceNotFoundException("Review Not Found"));
       return mapToReviewDto(review);
    }

    @Override
    public List<ReviewDto> getAllReviewsForUser(long trainerId){
        List<Review> reviews = reviewRepository.findByUserId(trainerId)
                .orElseThrow(()->new ResourceNotFoundException("No Reviews Available"));
        return reviews.stream()
                .map(review -> {
                    try {
                        return mapToReviewDto(review);
                    } catch (SQLException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                })
                .toList();
    }

    @Override
    public ReviewDto updateReview(long reviewId, ReviewCreateRequest request) throws SQLException {
        return reviewRepository.findById(reviewId)
                .map(review -> {
                    review.setFeedback(request.getFeedback());
                    review.setStars(request.getStars());
                    Review savedReview = reviewRepository.save(review);
                    try {
                        return mapToReviewDto(savedReview);
                    } catch (SQLException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }).orElseThrow(()->new ResourceNotFoundException("Review Not Found"));
    }

    @Override
    public void deleteReview(long reviewId) throws SQLException {
        reviewRepository.findById(reviewId)
                .ifPresentOrElse(reviewRepository::delete, () -> {
                    throw new ResourceNotFoundException("Review Not Found");
                });
    }

    //check
    @Override
    public int averageRatingForTrainer(long trainerId){
        double rating = getAllReviewsForUser(trainerId)
                .stream()
                .mapToDouble(ReviewDto::getStars)
                .average()
                .orElseThrow(()->new ResourceNotFoundException("No Reviews Available"));
        if(rating == 0.0){
            return 0;
        }else{
            return (int)Math.round(rating);
        }

    }

//    private ReviewDto mapToReviewDto(Review review) throws SQLException {
//        ReviewDto reviewDto = new ReviewDto();
//        reviewDto.setFeedback(review.getFeedback());
//        reviewDto.setStars(review.getStars());
//        reviewDto.setStudentId(review.getStudent().getId());
//        reviewDto.setTrainerId(review.getTrainer().getId());
//        reviewDto.setStudentName(review.getStudent().getFirstName() + " " + review.getStudent().getLastName());
//        reviewDto.setTrainerName(review.getTrainer().getFirstName() + " " + review.getTrainer().getLastName());
//        if(review.getTrainer().getPhoto().getImage() != null){
//            Blob studentBlob = review.getStudent().getPhoto().getImage();
//            int studentLength = (int) studentBlob.length();
//            byte[] studentBytes = studentBlob.getBytes(1, studentLength);
//            reviewDto.setStudentPhoto(studentBytes);
//        }else{
//            reviewDto.setStudentPhoto(null);
//        }
//
//      if(review.getTrainer().getPhoto().getImage() != null){
//          Blob trainerBlob = review.getTrainer().getPhoto().getImage();
//          int trainerLength = (int) trainerBlob.length();
//          byte[] trainerBytes = trainerBlob.getBytes(1, trainerLength);
//          reviewDto.setStudentPhoto(trainerBytes);
//      }else {
//          reviewDto.setStudentPhoto(null);
//      }
//        return reviewDto;
//    }

    private ReviewDto mapToReviewDto(Review review) throws SQLException {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setFeedback(review.getFeedback());
        reviewDto.setStars(review.getStars());
        reviewDto.setStudentId(review.getStudent().getId());
        reviewDto.setTrainerId(review.getTrainer().getId());
        reviewDto.setStudentName(review.getStudent().getFirstName() + " " + review.getStudent().getLastName());
        reviewDto.setTrainerName(review.getTrainer().getFirstName() + " " + review.getTrainer().getLastName());

        // Handle Student Photo
        if (review.getStudent().getPhoto() != null && review.getStudent().getPhoto().getImage() != null) {
            Blob studentBlob = review.getStudent().getPhoto().getImage();
            int studentLength = (int) studentBlob.length();
            byte[] studentBytes = studentBlob.getBytes(1, studentLength);
            reviewDto.setStudentPhoto(studentBytes);
        } else {
            reviewDto.setStudentPhoto(null);
        }

        // Handle Trainer Photo
        if (review.getTrainer().getPhoto() != null && review.getTrainer().getPhoto().getImage() != null) {
            Blob trainerBlob = review.getTrainer().getPhoto().getImage();
            int trainerLength = (int) trainerBlob.length();
            byte[] trainerBytes = trainerBlob.getBytes(1, trainerLength);
            reviewDto.setTrainerPhoto(trainerBytes); // Corrected to setTrainerPhoto
        } else {
            reviewDto.setTrainerPhoto(null); // Corrected to setTrainerPhoto
        }

        return reviewDto;
    }
}

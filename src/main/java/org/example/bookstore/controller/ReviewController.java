package org.example.bookstore.controller;

import org.example.bookstore.payload.ReviewDTO;
import org.example.bookstore.payload.request.ReviewCreate;
import org.example.bookstore.payload.request.ReviewUpdate;
import org.example.bookstore.payload.response.DataResponse;
import org.example.bookstore.service.Interface.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/createReview")
    public ResponseEntity<DataResponse> addReview(@RequestBody ReviewCreate reviewCreate) {

        ReviewDTO reviewDTO = reviewService.addReview(reviewCreate);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.CREATED.value())
                .status(HttpStatus.CREATED)
                .timestamp(LocalDateTime.now())
                .message("Review created!")
                .data(reviewDTO).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(dataResponse);
    }

    @PostMapping("/updateReview")
    public ResponseEntity<DataResponse> updateReview(@RequestBody ReviewUpdate reviewUpdate) {
        ReviewDTO reviewDTO1 = reviewService.updateReview(reviewUpdate);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .message("Review created!")
                .data(reviewDTO1)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @DeleteMapping("/deleteReview/{reviewId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<DataResponse> deleteReview(@PathVariable UUID reviewId) {
        String result = reviewService.deleteReview(reviewId);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .message("Review deleted!")
                .data(result).build();
        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @GetMapping("/getReviewByBookId/{bookId}")
    public ResponseEntity<DataResponse> getReviewByBookId(@PathVariable UUID bookId) {
        List<ReviewDTO> reviewDTOS = reviewService.getReviewsByBookId(bookId);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .message("Review found!")
                .data(reviewDTOS).build();

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @GetMapping("/getReviewById/{reviewId}")
    public ResponseEntity<DataResponse> getReviewById(@PathVariable UUID reviewId) {
        ReviewDTO reviewDTO = reviewService.getReviewById(reviewId);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .message("Review found!")
                .data(reviewDTO)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

    @GetMapping("/getReviewsByUserId/{userId}")
    public ResponseEntity<DataResponse> getReviewsByUserId(@PathVariable UUID userId) {
        List<ReviewDTO> reviewDTOS = reviewService.getReviewsByUserId(userId);
        DataResponse dataResponse = DataResponse.builder()
                .code(HttpStatus.OK.value())
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .message("Review found!")
                .data(reviewDTOS)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(dataResponse);
    }

}

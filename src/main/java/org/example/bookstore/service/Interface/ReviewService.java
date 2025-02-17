package org.example.bookstore.service.Interface;

import org.example.bookstore.payload.ReviewDTO;
import org.example.bookstore.payload.request.ReviewCreate;
import org.example.bookstore.payload.request.ReviewUpdate;

import java.util.List;
import java.util.UUID;

public interface ReviewService {

    ReviewDTO addReview(ReviewCreate reviewCreate);

    ReviewDTO updateReview(ReviewUpdate reviewUpdate);

    String deleteReview(UUID reviewId);

    List<ReviewDTO> getReviewsByUserId(UUID userId);

    ReviewDTO getReviewById(UUID id);

    List<ReviewDTO> getReviewsByBookId(UUID bookId);




}

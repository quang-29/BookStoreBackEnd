package org.example.bookstore.service;

import org.example.bookstore.enums.ErrorCode;
import org.example.bookstore.exception.AppException;
import org.example.bookstore.model.Book;
import org.example.bookstore.model.Review;
import org.example.bookstore.model.User;
import org.example.bookstore.payload.ReviewDTO;
import org.example.bookstore.payload.request.ReviewCreate;
import org.example.bookstore.payload.request.ReviewUpdate;
import org.example.bookstore.repository.BookRepository;
import org.example.bookstore.repository.ReviewRepository;
import org.example.bookstore.repository.UserRepository;
import org.example.bookstore.service.Interface.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    public ReviewServiceImpl(BookRepository bookRepository, UserRepository userRepository, ReviewRepository reviewRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ReviewDTO addReview(ReviewCreate reviewCreate) {

        Book book = bookRepository.findById(reviewCreate.getBookId())
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        User user = userRepository.findById(reviewCreate.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean exists = reviewRepository.existsByBookAndUser(book, user);
        if (exists) {
            throw new AppException(ErrorCode.REVIEW_ALREADY_EXISTS);
        }


        Review review = new Review();
        review.setBook(book);
        review.setCreatedAt(LocalDate.from(LocalDateTime.now()));
        review.setUser(user);
        review.setContent(reviewCreate.getContent());
        review.setRatePoint(reviewCreate.getRating());
        reviewRepository.save(review);

        List<Review> reviews = book.getReviews();
        reviews.add(review);
        book.setReviews(reviews);
        book.setAverageRating(getAvgRatingProduct(reviews));
        bookRepository.save(book);

        ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
        reviewDTO.setTitle(review.getBook().getTitle());
        reviewDTO.setUsername(review.getUser().getUsername());

        return reviewDTO;
    }

    @Transactional
    @Override
    public ReviewDTO updateReview(ReviewUpdate reviewUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Review review = reviewRepository.findById(reviewUpdate.getId())
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        if(!review.getUser().getUsername().equals(authentication.getName())) {
            throw new AppException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        UUID bookId = review.getBook().getId();
        Book book = bookRepository.findById(bookId)
                        .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));
        review.setContent(reviewUpdate.getContent());
        review.setRatePoint(reviewUpdate.getRatePoint());
        reviewRepository.save(review);

        List<Review> reviews = book.getReviews();
        reviews.add(review);
        book.setReviews(reviews);
        book.setAverageRating(getAvgRatingProduct(reviews));
        bookRepository.save(book);

        return modelMapper.map(review, ReviewDTO.class);
    }
    private double getAvgRatingProduct(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0;
        }
        double totalStars = 0;
        for (Review review : reviews) {
            totalStars += review.getRatePoint();
        }
        return totalStars / reviews.size();
    }
    @Override
    public String deleteReview(UUID reviewId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        boolean isAdmin = authentication.getAuthorities()
                .stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if(!isAdmin || !review.getUser().getUsername().equals(authentication.getName())) {
            throw new AppException(ErrorCode.REVIEW_ERROR_DELETE);
        }
        Book book = review.getBook();
        List<Review> reviewLists = book.getReviews();
        reviewLists.remove(review);
        book.setReviews(reviewLists);
        book.setAverageRating(getAvgRatingProduct(reviewLists));
        bookRepository.save(book);
        reviewRepository.delete(review);
        return "Delete review successfully";
    }

    @Override
    public List<ReviewDTO> getReviewsByUserId(UUID userId) {
        List<Review> reviews = reviewRepository.findAllReviewsByUserId(userId);
        return reviews.stream().map(review -> {
            ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
            reviewDTO.setTitle(review.getBook().getTitle());
            reviewDTO.setUsername(review.getUser().getUsername());
            return reviewDTO;
        }).toList();

    }

    @Override
    public ReviewDTO getReviewById(UUID reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_FOUND));
        ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
        reviewDTO.setTitle(review.getBook().getTitle());
        reviewDTO.setUsername(review.getUser().getUsername());
        return reviewDTO;
    }

    @Override
    public List<ReviewDTO> getReviewsByBookId(UUID bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        List<Review> reviews = book.getReviews();
        return reviews.stream().map(review ->{
            ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
            reviewDTO.setTitle(review.getBook().getTitle());
            reviewDTO.setUsername(review.getUser().getUsername());
            return reviewDTO;
        }).toList();

    }
}

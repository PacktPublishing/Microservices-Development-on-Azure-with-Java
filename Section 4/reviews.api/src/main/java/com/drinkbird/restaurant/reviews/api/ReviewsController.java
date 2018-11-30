package com.drinkbird.restaurant.reviews.api;

import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

@RestController
public class ReviewsController {
    private final ImageRepository imageRepository;
    private final ReviewsQueue reviewsQueue;

    public ReviewsController(ImageRepository imageRepository, ReviewsQueue reviewsQueue) {
        this.imageRepository = imageRepository;
        this.reviewsQueue = reviewsQueue;
    }

    @RequestMapping(value = "review",
                    method = RequestMethod.POST)
    public ReviewQueueMessage CreateReview(
        @RequestBody ReviewDto reviewDto
    ) {
        ImageBase64 image = reviewDto.getImageBase64();
        String imageName = UUID.randomUUID().toString();
        URI imageUri = imageRepository.Upload(imageName, image);

        ReviewQueueMessage reviewQueueMessage = new ReviewQueueMessage(
            reviewDto.getName(), reviewDto.getEmail(),
            reviewDto.getComment(), imageUri, DateTime.now());

        reviewsQueue.Send(reviewQueueMessage);
        return reviewQueueMessage;
    }
}

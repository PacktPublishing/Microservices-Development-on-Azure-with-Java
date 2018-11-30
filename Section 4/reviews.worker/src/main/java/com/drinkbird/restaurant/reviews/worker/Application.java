package com.drinkbird.restaurant.reviews.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private final ReviewsQueue reviewsQueue;
    private final ReviewHandler reviewHandler;
    private final ExecutorService executor;

	public Application(ReviewsQueue queue, ReviewHandler reviewHandler) {
	    this.reviewsQueue = queue;
	    this.reviewHandler = reviewHandler;
	    this.executor = Executors.newSingleThreadExecutor();
    }

    @Override
    public void run(String... args) throws Exception {
	    logger.info("Start receiving from reviewsQueue");
        reviewsQueue.startReceiving(executor, reviewHandler);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static Logger logger = LoggerFactory.getLogger(Application.class);
}

package com.drinkbird.restaurant.reviews.worker;

import com.drinkbird.restaurant.reviews.worker.sentiment.SentimentAnalyzer;
import com.drinkbird.restaurant.reviews.worker.sentiment.SentimentResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;

@Service
public class ReviewHandler {
    private static final double minimumSentimentScore = 0.8;

    private final SentimentAnalyzer analyzer;
    private final EmailAlerter alerter;

    public ReviewHandler(SentimentAnalyzer analyzer, EmailAlerter alerter) {
        this.analyzer = analyzer;
        this.alerter = alerter;
    }

    public void handle(Review review) throws IOException {
        SentimentResult sentiment = analyzer.analyze(review.getComment());
        if (sentiment.getErrors().length > 0) {
            logger.error("Unable to analyze sentiment: {}",
                Arrays.toString(sentiment.getErrors()));
        }
        else {
            double score = sentiment.getDocuments()[0].getScore();

            if (score < minimumSentimentScore) {
                logger.info("Unsatisfied customer, alerting! name={}|score={}|on={}|comment={}",
                    review.getName(), score, review.getReviewedOn(), review.getComment());

                alerter.alert(review, score);
            }
            else {
                logger.info("Satisfied customer. name={}|score={}|on={}|comment={}",
                    review.getName(), score, review.getReviewedOn(), review.getComment());
            }
        }
    }

    private static Logger logger = LoggerFactory.getLogger(ReviewHandler.class);
}

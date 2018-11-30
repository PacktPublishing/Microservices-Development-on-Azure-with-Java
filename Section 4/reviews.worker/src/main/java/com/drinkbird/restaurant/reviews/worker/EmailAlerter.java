package com.drinkbird.restaurant.reviews.worker;

import com.sendgrid.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
public class EmailAlerter {
    private final Email fromEmail;
    private final Email toEmail;
    private final SendGrid sender;

    public EmailAlerter(Environment env) {
        this.fromEmail = new Email(Objects.requireNonNull(
            env.getProperty("azure.sendGrid.fromEmail")));
        this.toEmail = new Email(Objects.requireNonNull(
            env.getProperty("azure.sendGrid.toEmail")));

        String apiKey = Objects.requireNonNull(
            env.getProperty("azure.sendGrid.key"));
        this.sender = new SendGrid(apiKey);
    }

    public void alert(Review review, double sentimentScore) throws IOException {
        Mail emailAlert = composeAlert(review, sentimentScore);
        Response response = sendAlert(emailAlert);
        logger.info("Sent alert for user: {}", review.getName(),
            response.getStatusCode(), response.getBody());
    }

    private Mail composeAlert(Review review, double sentimentScore) {
        String subject = String.format(
            "A customer needs your attention: %s", review.getName());
        Content content = new Content("text/plain", String.format(
            "On %s, user %s with email %s wrote: %s - sentiment score: %f",
            review.getReviewedOn().toDateTime(), review.getName(),
            review.getEmail(), review.getComment(), sentimentScore));
        return new Mail(this.fromEmail, subject, this.toEmail, content);
    }

    private Response sendAlert(Mail mail) throws IOException {
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        return this.sender.api(request);
    }

    private static Logger logger = LoggerFactory.getLogger(EmailAlerter.class);
}

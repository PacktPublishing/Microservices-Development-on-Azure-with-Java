package com.drinkbird.restaurant.reviews.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.microsoft.azure.servicebus.Message;
import com.microsoft.azure.servicebus.QueueClient;
import com.microsoft.azure.servicebus.ReceiveMode;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Service
public class ReviewsQueue {
    private final String connectionString;
    private final String queueName;
    private final ObjectMapper mapper = new ObjectMapper();

    public ReviewsQueue(Environment env) {
        this.connectionString =
            env.getProperty("azure.serviceBus.connectionString");
        this.queueName =
            env.getProperty("azure.serviceBus.reviewsQueueName");

        this.mapper.registerModule(new JodaModule());
        this.mapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
        this.mapper.setDateFormat(
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
    }

    public void Send(ReviewQueueMessage review) {
        try {
            QueueClient sendClient = new QueueClient(
                new ConnectionStringBuilder(connectionString, queueName),
                ReceiveMode.PEEKLOCK);

            String reviewJson = mapper.writeValueAsString(review);
            Message message = new Message(reviewJson);
            message.setContentType("application/json");

            sendClient.send(message);
            sendClient.close();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

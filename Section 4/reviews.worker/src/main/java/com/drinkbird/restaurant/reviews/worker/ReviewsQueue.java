package com.drinkbird.restaurant.reviews.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.microsoft.azure.servicebus.*;

import com.microsoft.azure.servicebus.primitives.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Function;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@Service
public class ReviewsQueue {
    private final QueueClient receiveClient;
    private final ObjectMapper mapper;
    private final CountDownLatch latch;

    public ReviewsQueue(Environment env)
        throws ServiceBusException, InterruptedException
    {
        String connectionString = Objects.requireNonNull(
            env.getProperty("azure.serviceBus.connectionString"));
        String queueName = Objects.requireNonNull(
            env.getProperty("azure.serviceBus.reviewsQueueName"));
        this.receiveClient = new QueueClient(
            new ConnectionStringBuilder(connectionString, queueName),
            ReceiveMode.PEEKLOCK);

        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JodaModule());
        this.mapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
        this.mapper.setDateFormat(
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));

        this.latch = new CountDownLatch(1);
    }

    void startReceiving(ExecutorService executor, ReviewHandler reviewHandler)
        throws Exception
    {
        IMessageHandler handler = new IMessageHandler() {
            public CompletableFuture<Void> onMessageAsync(IMessage message)
            {
                if (message != null) {
                    String messageBody = new String(message.getBody());
                    try {
                        logger.debug("Received message: {}", messageBody);
                        Review review =  mapper.readValue(messageBody, Review.class);
                        reviewHandler.handle(review);
                    }
                    catch (IOException ex) {
                        logger.error("Error processing message: {}", messageBody, ex);
                    }
                }
                return CompletableFuture.completedFuture(null);
            }

            public void notifyException(
                Throwable exception, ExceptionPhase exceptionPhase)
            {
                logger.error("Error processing message: {}-{}",
                    exceptionPhase, exception.getMessage(), exception);
            }
        };

        MessageHandlerOptions options = new MessageHandlerOptions(
            1, true, Duration.ofMinutes(1));

        receiveClient.registerMessageHandler(handler, options, executor);

        // block indefinitely
        latch.await();
    }

    private static Logger logger = LoggerFactory.getLogger(ReviewsQueue.class);
}

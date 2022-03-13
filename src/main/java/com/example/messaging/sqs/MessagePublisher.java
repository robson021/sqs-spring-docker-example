package com.example.messaging.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Profile("sqs")
public class MessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(MessagePublisher.class);

    private final ObjectWriter objectWriter;

    private final AmazonSQS sqs;

    private final String queueUrl;

    public MessagePublisher(ObjectMapper objectMapper, AmazonSQS sqs, @Value("${sqs.url}") String url) {
        this.sqs = sqs;
        this.queueUrl = url;
        this.objectWriter = objectMapper.writer();
    }

    @Scheduled(fixedDelay = 5_000)
    public void publishMessage() {
        try {
            doPublish();
        } catch (Exception e) {
            log.error("Failed to publish message.", e);
        }
    }

    private void doPublish() throws JsonProcessingException {
        var message = objectWriter.writeValueAsString(new GpsData(1, Math.random(), Math.random(), System.currentTimeMillis()));
        sqs.sendMessage(queueUrl, message);
        log.info("Published to '{}'. Message: {}", queueUrl, message);
    }

}

package com.example.messaging.sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Profile("sqs")
public class MessagePublisher {

    private static final Logger log = LoggerFactory.getLogger(MessagePublisher.class);

    private final AmazonSQS sqs;

    private final String queueUrl;

    public MessagePublisher(AmazonSQS sqs, @Value("sqs.url") String url) {
        this.sqs = sqs;
        this.queueUrl = url;
    }

    // FIXME: sqs client config
//    @Scheduled(fixedDelay = 5_000)
    public void publishMessage() {
        try {
            doPublish();
        } catch (Exception e) {
            log.error("Failed to publish message.", e);
        }
    }

    private void doPublish() {
        var message = "Random message " + UUID.randomUUID();
        sqs.sendMessage(queueUrl, message);
        log.info("Published to '{}'. Message: {}", queueUrl, message);
    }

}

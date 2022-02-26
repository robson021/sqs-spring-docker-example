package com.example.messaging.sqs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("sqs")
public class MessageListener {

    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    @SqsListener(value = "${sqs.url}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void receiveMessage(String message) {
        log.info("Received message: {}", message);
    }

}
package com.example.messaging.sqs;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.SimpleMessageListenerContainerFactory;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.support.destination.DynamicQueueUrlDestinationResolver;
import org.springframework.context.annotation.*;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;

import java.util.List;

@EnableSqs
@Configuration
@Profile("sqs")
public class SqsConfig {

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync(){
        return AmazonSQSAsyncClientBuilder.standard()
                .withRegion(Regions.DEFAULT_REGION)
                .build();
    }

    @Bean
    public MappingJackson2MessageConverter mappingJackson2MessageConverter() {
        var messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setStrictContentTypeMatch(false);
        messageConverter.setObjectMapper(new ObjectMapper());
        return messageConverter;
    }

    @Bean
    public DynamicQueueUrlDestinationResolver destinationResolver(AmazonSQSAsync amazonSQSAsync, @Value("${example.sqs.url}") String url) {
        var destinationResolver = new DynamicQueueUrlDestinationResolver(amazonSQSAsync);
        destinationResolver.resolveDestination(url);
        destinationResolver.setAutoCreate(true);
        return destinationResolver;
    }

    @Lazy
    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(
            AmazonSQSAsync amazonSQSAsync,
            DynamicQueueUrlDestinationResolver destinationResolver,
            MappingJackson2MessageConverter messageConverter) {
        return new QueueMessagingTemplate(amazonSQSAsync, destinationResolver, messageConverter);
    }

    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory(MappingJackson2MessageConverter messageConverter)  {
        var factory = new QueueMessageHandlerFactory();
        factory.setArgumentResolvers(List.of(new PayloadMethodArgumentResolver(messageConverter)));
        return factory;
    }

    @Bean
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSqs) {
        var factory = new SimpleMessageListenerContainerFactory();
        factory.setAmazonSqs(amazonSqs);
        factory.setMaxNumberOfMessages(1);
        factory.setWaitTimeOut(10);
        return factory;
    }


}

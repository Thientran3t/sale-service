package com.vnet.solution.consumer.services;

import com.vnet.solution.common.dto.SalesData;
import com.vnet.solution.common.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KafkaConsumer {

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public KafkaConsumer(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = Constants.KAFKA_TOPIC_NAME)
    public void consume(List<SalesData> message) {
        LOGGER.info(String.format("Message received -> %s", message.toString()));
        simpMessagingTemplate.convertAndSend("/vnet", message);
    }

}

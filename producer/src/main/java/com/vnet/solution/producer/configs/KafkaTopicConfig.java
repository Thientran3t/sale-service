package com.vnet.solution.producer.configs;

import com.vnet.solution.common.util.Constants;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic vnetTopic(){
        return TopicBuilder.name(Constants.KAFKA_TOPIC_NAME)
                .partitions(5)
                .replicas(1)
                .build();
    }

}

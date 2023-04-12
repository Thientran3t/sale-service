package com.vnet.solution.producer.configs;

import com.vnet.solution.producer.services.KafkaProducer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/vnet")
public class TestController {

    private KafkaProducer kafkaProducer;

    public TestController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/test")
    @CrossOrigin
    public ResponseEntity test() {
        try {
            this.kafkaProducer.readAllFiles();
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (IOException | InterruptedException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

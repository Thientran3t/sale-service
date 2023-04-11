package com.vnet.solution.producer;

import com.vnet.solution.common.dto.SalesData;
import com.vnet.solution.consumer.services.KafkaConsumer;
import com.vnet.solution.producer.services.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
//@DirtiesContext
//@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class ProducerApplicationTests {

	@TestConfiguration
	public static class TestConfig {

		@Bean
		public KafkaConsumer consumer() {
			return new KafkaConsumer(null);
		}
	}

	@Autowired
	private KafkaProducer producer;
	@Autowired
	private KafkaConsumer consumer;

	@Test
	void givenEmbeddedKafkaBroker_whenSendingWithSimpleProducer_thenMessageReceived() {
		SalesData salesData = new SalesData();
		salesData.setSalesDate(LocalDate.now());
		salesData.setSalesRevenue(BigDecimal.TEN);
		salesData.setSalesUnit(10);
		salesData.setProductName("TestProductName");
		salesData.setStoreName("TestName");

		producer.sendMessage(Arrays.asList(salesData));
	}

	@Test
	void readFileCsv() throws IOException {
		producer.readFileCsv();
	}
}

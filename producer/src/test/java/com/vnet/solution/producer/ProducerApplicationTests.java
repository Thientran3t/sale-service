package com.vnet.solution.producer;

import com.vnet.solution.common.dto.SalesData;
import com.vnet.solution.common.dto.Tuple;
import com.vnet.solution.producer.services.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class ProducerApplicationTests {
	@Autowired
	private KafkaProducer producer;
	@Test
	void testSendMessage() {
		SalesData salesData = new SalesData();
		salesData.setSalesDate(LocalDate.now());
		salesData.setSalesRevenue(BigDecimal.TEN);
		salesData.setSalesUnit(10);
		salesData.setProductName("TestProductName");
		salesData.setStoreName("TestName");

		producer.sendMessage(Arrays.asList(salesData));
	}

	@Test
	void testReadFileCsvAndAggregatingData() throws IOException {
		Resource resource = new ClassPathResource("Sales_20221201_20221231.psv");
		File file = resource.getFile();
		var result = producer.readFileCsv(file);
		Tuple tuple = new Tuple("Apple iPad 9", "Store5");
		assertNotNull(result);
		assertInstanceOf(Map.class, result);
		assertTrue(result.containsKey(tuple));

		var data = producer.aggregateData(result);
		assertNotNull(data);
		assertInstanceOf(List.class, data);
		SalesData salesData = new SalesData("Store5", "Apple iPad 9", 138, BigDecimal.valueOf(60561.4530));
		data.contains(salesData);
	}
}

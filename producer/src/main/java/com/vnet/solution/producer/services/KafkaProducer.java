package com.vnet.solution.producer.services;

import com.vnet.solution.common.dto.SalesData;
import com.vnet.solution.common.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@Service
public class KafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);
    @Value("${origin.source.path}")
    private String originFilePath;
    @Value("${sequence.time.seconds}")
    private int sequenceTime;
    @Value("${archive.source.path}")
    private String archiveFilePath;
    private KafkaTemplate<String, SalesData> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, SalesData> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(List<SalesData> data){
        LOGGER.info(String.format("Message sent -> %s", data.toString()));
        Message<List<SalesData>> message = MessageBuilder
                        .withPayload(data)
                        .setHeader(KafkaHeaders.TOPIC, Constants.KAFKA_TOPIC_NAME)
                        .build();

        kafkaTemplate.send(message);
    }

    public void readAllFiles() throws InterruptedException, IOException {
        final File folder = new File(originFilePath);
        if (folder.listFiles().length == 0) return;
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isFile()) {
                this.readFileCsv(fileEntry);
                moveReadFile(fileEntry, archiveFilePath);
                Thread.sleep(sequenceTime * 1000);
            }
        }
    }

    public void moveReadFile(File file, String targetPath) throws IOException {
        final Path temp = Files.move(Paths.get(file.getPath()), Paths.get(targetPath + file.getName()));
        if (temp != null) {
            LOGGER.info("File moved successfully");
            return;
        }
        throw new IllegalArgumentException(String.format("Couldn't move file to folder %s", targetPath));
    }

    public void readFileCsv(File inputF){
        try(InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS))) {
            List<SalesData> items = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
            Map<Tuple, List<SalesData>> records =
                items
                        .stream()
                        .collect(groupingBy(item -> new Tuple(item.getProductName(), item.getStoreName())));
            aggregateData(records);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void aggregateData(Map<Tuple, List<SalesData>> records) {
        if (records.isEmpty()) return;
        List<SalesData> message = records.entrySet().stream()
            .flatMap(e -> Stream.of(e.getValue()))
            .map(
                item -> {
                    int unitSales = item.stream().mapToInt(SalesData::getSalesUnit).sum();
                    BigDecimal revenue = item.stream()
                            .map(SalesData::getSalesRevenue)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new SalesData(LocalDate.now(), item.get(0).getStoreName(), item.get(0).getProductName(), unitSales, revenue);
                })
            .collect(toList());
        this.sendMessage(message);
    }

    private Function<String, SalesData> mapToItem = (line) -> {
        String[] values = line.split("\\|");

        SalesData item = new SalesData();
        item.setSalesDate(LocalDate.parse(values[0], DateTimeFormatter.BASIC_ISO_DATE));
        item.setStoreName(values[1]);
        item.setProductName(values[2]);
        item.setSalesUnit(Integer.valueOf(3));
        item.setSalesRevenue(new BigDecimal(values[4]));

        return item;
    };

    class Tuple {
        private String product;
        private String store;

        public Tuple(String product, String store) {
            this.product = product;
            this.store = store;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple tuple = (Tuple) o;
            return Objects.equals(product, tuple.product) && Objects.equals(store, tuple.store);
        }

        @Override
        public int hashCode() {
            return Objects.hash(product, store);
        }
    }

}

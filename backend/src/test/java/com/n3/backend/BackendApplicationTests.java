package com.yourcompany.yourapp;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@EmbeddedKafka(partitions = 1, topics = {"test-topic"})
public class BackendApplicationTests {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void testEmbeddedKafka() {
        // Gửi một thông điệp vào Kafka topic
        kafkaTemplate.send("test-topic", "Hello, Embedded Kafka!");

        // Khởi tạo consumer để đọc tin nhắn
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(KafkaTestUtils.consumerProps("test-group", "false", "localhost:9092"),
                new StringDeserializer(), new StringDeserializer());

        // Đảm bảo rằng consumer có thể nhận được tin nhắn
        consumer.subscribe(Collections.singleton("test-topic"));
        consumer.poll(Duration.ofMillis(1000));

        // Kiểm tra số lượng tin nhắn đã nhận
        assertThat(consumer.poll(Duration.ofMillis(1000)).count()).isGreaterThan(0);
    }
}

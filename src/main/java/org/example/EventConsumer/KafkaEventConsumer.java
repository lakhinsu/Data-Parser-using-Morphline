package org.example.EventConsumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.example.MorphlineProcessor.MorphlineProcessor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaEventConsumer implements Runnable {
    static Logger log = LoggerFactory.getLogger(KafkaEventConsumer.class.getName());
    private final KafkaConsumer<String, String> consumer;
    private final ArrayList<String> topics;
    private final CountDownLatch shutdownLatch;

    private final MorphlineProcessor morphlineProcessor;

    private final String morphlineId;

    public KafkaEventConsumer(Properties kafkaProperties, ArrayList<String> topics, String morphlineFilePath, String morphlineId) {
        this.consumer = new KafkaConsumer<String, String>(kafkaProperties);
        this.morphlineId = morphlineId;
        this.topics = topics;
        this.shutdownLatch = new CountDownLatch(1);
        this.morphlineProcessor = new MorphlineProcessor(morphlineFilePath, morphlineId);
    }

    public void process(String record) {
       this.morphlineProcessor.Process(record);
    };

    public void run() {
        try {
            log.info("Running consumer for topics" + this.topics.toString());
            consumer.subscribe(topics);

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
                records.forEach(record -> {
                    log.info("Message received on topic: " + record.topic());
                    log.info("Using morphline: " + this.morphlineId);
                    process(record.value());
                });
            }
        } catch (WakeupException e) {
            // ignore, we're closing
            log.info("Closing consumer");
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            consumer.close();
            shutdownLatch.countDown();
        }
    }

    public void shutdown() throws InterruptedException {
        consumer.wakeup();
        shutdownLatch.await();
    }
}
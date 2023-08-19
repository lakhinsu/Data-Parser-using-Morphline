package org.example;

import org.example.Config.ConsumerConfig;
import org.example.Config.KafkaConfig;
import org.example.EventConsumer.KafkaEventConsumer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    static Logger log = LoggerFactory.getLogger(Main.class.getName());
    public static void main(String[] args) {
        String kafkaConfigFile = System.getenv("KAFKA_PROPERTIES_FILE");
        if (kafkaConfigFile.isEmpty()) {
            log.info("Kafka config file not found....");
            System.exit(1);
        }
        String appConfig = System.getenv("APP_CONFIG_FILE");
        if (appConfig.isEmpty()) {
            log.info("App config file not found....");
            System.exit(1);
        }
        KafkaConfig config = new KafkaConfig(kafkaConfigFile);
        try {
            ConsumerConfig consumerConfig = new ConsumerConfig(appConfig);
            ExecutorService pool = Executors.newFixedThreadPool(consumerConfig.getConfigProperties().size());
            consumerConfig.getConfigProperties().forEach(configProperty -> {
                try {
                    KafkaEventConsumer kafkaEventConsumer = new KafkaEventConsumer(config.getConfig(), configProperty.getTopics(), configProperty.getMorphlinePath(), configProperty.getMorphlineId());
                    pool.execute(kafkaEventConsumer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            log.info("Error occurred while parsing config file");
            log.error(e.getMessage());
            System.exit(1);
        }
    }
}
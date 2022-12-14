package com.example.kafkatutorial.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageHandler {


    @KafkaListener(id = "consumer-2", groupId = "test-grop-02", topics = {"test"})
    public void listenerOne2(ConsumerRecord<String, String> record) {
        if (record != null) {
            try {

                String value = record.value();
                log.info("consumer-2 received : {}",value);
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }
    }
    @KafkaListener(id = "consumer-4", groupId = "test-grop-04", topics = {"test"},containerFactory = "ackContainerFactory")
    public void listenerOne3(ConsumerRecord<String,String> record, Acknowledgment acknowledgment) {
        if (record != null) {
            try {
                String value = record.value();
                log.info("consumer-3 received : {}",value);
            } catch (Exception e) {
                log.error(e.getMessage());
            }finally {
                acknowledgment.acknowledge();
            }

        }
    }
}
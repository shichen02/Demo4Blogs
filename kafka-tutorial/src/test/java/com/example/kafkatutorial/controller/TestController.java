package com.example.kafkatutorial.controller;

import com.example.kafkatutorial.KafkaTutorialApplicationTests;
import javax.annotation.Resource;
import org.junit.Test;
import org.springframework.kafka.core.KafkaTemplate;


public class TestController extends KafkaTutorialApplicationTests {

    @Resource
    KafkaTemplate kafkaTemplate;


    @Test
    public void testSend() {
        kafkaTemplate.send("test", "hello,kafka...");
    }


}

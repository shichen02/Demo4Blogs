package com.example.kafkatutorial.controller;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @Resource
    KafkaTemplate kafkaTemplate;


    @GetMapping("/sendMsg/{msg}")
    public void testSend(@PathVariable("msg") String msg) {
        log.info("send msg : {}", msg);
        kafkaTemplate.send("test", msg);
    }


}

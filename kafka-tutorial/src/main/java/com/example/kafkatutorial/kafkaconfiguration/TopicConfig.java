package com.example.kafkatutorial.kafkaconfiguration;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

/**
 * @author smile
 * @date 2022-07-14 10:54
 */
@Configuration
public class TopicConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;

    @Bean
    public KafkaAdmin kafkaAdmin(){
        Map<String, Object> configs  = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        return  new KafkaAdmin(configs);
    }

    @Bean
    public AdminClient adminClient(){
        return   AdminClient.create(kafkaAdmin().getConfigurationProperties());
    }
}

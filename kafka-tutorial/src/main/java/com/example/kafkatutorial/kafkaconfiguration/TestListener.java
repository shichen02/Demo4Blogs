//package com.example.kafkatutorial.configuration;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.riie.dataserve.tdengine.PointValue;
//import org.riie.dataserve.tdengine.PointValueService;
//import org.riie.dataserve.utils.FastJsonUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * 监控数据监听
// *
// * @author smile
// * @date 2022-07-14 11:01
// */
//@Component
//public class TestListener {
//    private static final Logger logger = LoggerFactory.getLogger(TestListener.class);
//
//    @Autowired
//    private PointValueService pointValueService;
//
//    /**
//     * 单条记录消费
//    */
////    @KafkaListener(id = "consu1",  topics = {"test"})
//    public void listenerMessage(ConsumerRecord<String, String> record) {
//        logger.info("接收到kafka消息键为:{},消息值为:{},消息头为:{},消息分区为:{},消息主题为:{}", record.key(), record.value(), record.headers(), record.partition(), record.topic());
//    }
//
//    /**
//     * 批量消费
//    */
////    @KafkaListener(id = "consg1", topics = {"test"}, containerFactory = "batchFactory")
//    public void listen(List<ConsumerRecord<?, ?>> records) {
//        try {
//            for (ConsumerRecord<?, ?> record : records) {
//                Optional<?> kafkaMessage = Optional.ofNullable(record.value());
//                if (kafkaMessage.isPresent()) {
//                    Object message = record.value();
//                    String jsonstr = message.toString();
//                    System.out.println("==============="+jsonstr);
//                }
//            }
//        } catch (Exception e) {
//            logger.warn(e.getMessage());
//        }
//    }
//
//
//    /**
//     * 批量消费
//     */
////    @KafkaListener(id = "test1", groupId = "test", topics = {"test1"}, containerFactory = "batchFactory")
//    public void listenTest(List<ConsumerRecord<String, String>> records) {
//        try {
//            List<PointValue> values = new ArrayList<>();
//            logger.info("=================="+records.size());
//            for (ConsumerRecord<String, String> record : records) {
//                Optional<String> kafkaMessage = Optional.ofNullable(record.value());
//                if (kafkaMessage.isPresent()) {
//                    String value = record.value();
//                    List<PointValue> points = FastJsonUtils.jsonToList(value, PointValue.class);
//                    values.addAll(points);
//                }
//            }
//            pointValueService.saveValue(values, 1000);
//        } catch (Exception e) {
//            logger.warn(e.getMessage());
//        }
//    }
//
//    /**
//     * 手动提交偏移量
//    */
////    @KafkaListener(id = "consu3", topics = {"test"}, containerFactory = "batchFactory")
////    public void listener(List<ConsumerRecord<?, ?>> records,  Acknowledgment ack) {
////        System.out.println("-============");
////        try {
////            for (ConsumerRecord<?, ?> record : records) {
////                System.out.println("-============");
////                Optional<?> kafkaMessage = Optional.ofNullable(record.value());
////                if (kafkaMessage.isPresent()) {
////                    Object message = record.value();
////                    String jsonstr = message.toString();
////                    System.out.println("==============="+jsonstr);
////                }
////            }
////            //手动提交offset
////            ack.acknowledge();
////        } catch (Exception e) {
////            logger.warn(e.getMessage());
////        }
////    }
//}

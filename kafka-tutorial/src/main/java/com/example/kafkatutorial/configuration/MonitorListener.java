//package com.example.kafkatutorial.configuration;
//
//import com.example.kafkatutorial.service.PointValueService;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.riie.dataserve.tdengine.PointValue;
//import org.riie.dataserve.utils.FastJsonUtils;
//import org.riie.dataserve.utils.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
///**
// * 监控数据监听
// *
// * @author smile
// * @date 2022-07-14 11:01
// */
//@Component
//public class MonitorListener {
//    private static final Logger logger = LoggerFactory.getLogger(MonitorListener.class);
//
//    @Autowired
//    private PointValueService pointValueService;
//
//    /**
//     * groupId 相同，可以设多个id
//     *
//     * @param record:
//     * @return: void
//     * @author: smile
//     */
//
////    @KafkaListener(id = "consuone1", groupId = "consuone", topics = {"real"})
//    public void listenerOne1(ConsumerRecord<String, String> record) {
//        if (record != null) {
//            try {
//
//                String value = record.value();
//                if (StringUtils.isNotNull(value)) {
//                    PointValue pointValue = FastJsonUtils.jsonToBean(value, PointValue.class);
//                    pointValue.setUpdateTime(new Date());
//                    pointValueService.saveValue(pointValue);
//                }
//            } catch (Exception e) {
//                logger.error(e.getMessage());
//            }
//
//        }
//    }
//
//    /**
//     * @param record:
//     * @return: void
//     * @author: smile
//     */
//
////    @KafkaListener(id = "consuone2", groupId = "consuone", topics = {"real"})
//    public void listenerOne2(ConsumerRecord<String, String> record) {
//        if (record != null) {
//            try {
//
//                String value = record.value();
//                if (StringUtils.isNotNull(value)) {
//                    PointValue pointValue = FastJsonUtils.jsonToBean(value, PointValue.class);
//                    pointValue.setUpdateTime(new Date());
//                    pointValueService.saveValue(pointValue);
//                }
//            } catch (Exception e) {
//                logger.error(e.getMessage());
//            }
//
//        }
//    }
//
//    /**
//     * 批量消费
//     *
//     * @param records:
//     * @return: void
//     * @author: smile
//     */
//
//    @KafkaListener(id = "consbatch1", groupId = "consbatch", topics = {"real"}, containerFactory = "batchFactory")
//    public void listenBatch1(List<ConsumerRecord<String, String>> records) {
//        try {
//            List<PointValue> values = new ArrayList<>();
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
//     * 批量消费
//     */
//    @KafkaListener(id = "consbatch2", groupId = "consbatch", topics = {"real"}, containerFactory = "batchFactory")
//    public void listenBatch2(List<ConsumerRecord<String, String>> records) {
//        try {
//            List<PointValue> values = new ArrayList<>();
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
//     * 批量消费
//     */
//    @KafkaListener(id = "consbatch3", groupId = "consbatch", topics = {"real"}, containerFactory = "batchFactory")
//    public void listenBatch3(List<ConsumerRecord<String, String>> records) {
//        try {
//            List<PointValue> values = new ArrayList<>();
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
//     * 批量消费
//     */
//    @KafkaListener(id = "consbatch4", groupId = "consbatch", topics = {"real"}, containerFactory = "batchFactory")
//    public void listenBatch4(List<ConsumerRecord<String, String>> records) {
//        try {
//            List<PointValue> values = new ArrayList<>();
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
//
//}

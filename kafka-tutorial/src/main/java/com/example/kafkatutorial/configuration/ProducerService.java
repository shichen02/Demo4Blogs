package com.example.kafkatutorial.configuration;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * 生产服务
 *
 * @author smile
 * @date 2022-07-14 15:35
 */
@Component
public class ProducerService {
    private static final Logger logger = LoggerFactory.getLogger(ProducerService.class);

    @Resource
    private KafkaTemplate<String, Object> kafkaTemplate;


    /**
     * 发送消息（同步）
     *
     * @param topic   主题
     * @param key     键
     * @param message 值
     */
    public void sendSync(String topic, String key,  Object message) throws InterruptedException, ExecutionException, TimeoutException {
        //可以指定最长等待时间，也可以不指定
        kafkaTemplate.send(topic, key, message).get(10, TimeUnit.SECONDS);

    }

    /**
     *
     */
    public void sendSync(String topic, Object message) throws InterruptedException, ExecutionException, TimeoutException {
        //可以指定最长等待时间，也可以不指定
        kafkaTemplate.send(topic, message).get(10, TimeUnit.SECONDS);
    }

    /**
     * 发送消息（异步）
     *
     * @param topic   主题
     * @param message 消息内容
     */
    public void sendAsync(String topic, Object message) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);
        //添加回调
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.error("sendMessageAsync failure! topic : {}, message: {}", topic, message);
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringStringSendResult) {

            }
        });
    }

    /**
     * 发送消息（异步）
     *
     * @param key     键
     * @param topic   主题
     * @param message 消息内容
     */
    public void sendAsync(String topic, String key, Object message) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, message);
        //添加回调
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.error("sendMessageAsync failure! topic : {}, message: {}", topic, message);
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringStringSendResult) {

            }
        });
    }


}

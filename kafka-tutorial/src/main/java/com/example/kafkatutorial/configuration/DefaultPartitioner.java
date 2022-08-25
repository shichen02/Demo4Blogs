package com.example.kafkatutorial.configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.utils.Utils;

/**
 * 分区策略
 *
 * @author smile
 * @date 2022-07-26 17:43
 */
public class DefaultPartitioner implements Partitioner {

    private final ConcurrentMap<String, AtomicInteger> topicCounterMap = new ConcurrentHashMap<>();

    /**
     * Compute the partition for the given record.
     *
     * @param topic      The topic name
     * @param key        The key to partition on (or null if no key)
     * @param keyBytes   serialized key to partition on (or null if no key)
     * @param value      The value to partition on or null
     * @param valueBytes serialized value to partition on or null
     * @param cluster    The current cluster metadata
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        /* 首先通过cluster从元数据中获取topic所有的分区信息 */
        List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
        //拿到该topic的分区数
        int numPartitions = partitions.size();
        //如果消息记录中没有指定key
        if (keyBytes == null) {
            //则获取一个自增的值
            int nextValue = nextValue(topic);
            //通过cluster拿到所有可用的分区（可用的分区这里指的是该分区存在首领副本）
            List<PartitionInfo> availablePartitions = cluster.availablePartitionsForTopic(topic);
            //如果该topic存在可用的分区
            if (availablePartitions.size() > 0) {
                //那么将nextValue转成正数之后对可用分区数进行取余
                int part = Utils.toPositive(nextValue) % availablePartitions.size();
                //然后从可用分区中返回一个分区
                return availablePartitions.get(part).partition();
            } else { // 如果不存在可用的分区
                //那么就从所有不可用的分区中通过取余的方式返回一个不可用的分区
                return Utils.toPositive(nextValue) % numPartitions;
            }
        } else { // 如果消息记录中指定了key
            // 则使用该key进行hash操作，然后对所有的分区数进行取余操作，这里的hash算法采用的是murmur2算法，然后再转成正数
            //toPositive方法很简单，直接将给定的参数与0X7FFFFFFF进行逻辑与操作。
            return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions;
        }
    }

    @Override
    public void close() {

    }

    //nextValue方法可以理解为是在消息记录中没有指定key的情况下，需要生成一个数用来代替key的hash值
    //方法就是最开始先生成一个随机数，之后在这个随机数的基础上每次请求时均进行+1的操作
    private int nextValue(String topic) {
        //每个topic都对应着一个计数
        AtomicInteger counter = topicCounterMap.get(topic);
        if (null == counter) { // 如果是第一次，该topic还没有对应的计数
            //那么先生成一个随机数
            counter = new AtomicInteger(ThreadLocalRandom.current().nextInt());
            //然后将该随机数与topic对应起来存入map中
            AtomicInteger currentCounter = topicCounterMap.putIfAbsent(topic, counter);
            if (currentCounter != null) {
                //之后把这个随机数返回
                counter = currentCounter;
            }
        }
        //一旦存入了随机数之后，后续的请求均在该随机数的基础上+1之后进行返回
        return counter.getAndIncrement();
    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
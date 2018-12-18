package com.bin.kafka;

import com.bin.kafka.serializer.avro.User;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Consumer {
    public static void main(String[] args) {

        //Kafka consumer configuration settings
        String topicName = "fortest";
        Properties props = new Properties();

        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "haha");//消费者分组ID
        props.put("enable.auto.commit", "true");//设置偏移量提交类型 当前是自动提交，可以设置为手动控制，来决定一条消息是否消费完成。
        props.put("auto.commit.interval.ms", "1000");//偏移量自动提交的间隔时间
        props.put("max.poll.interval.ms", "1000");//当消费者poll调用的频率大于此设置的最大间隔，客户端将主动离开组，并将导致offset设置失败，因此要留在组中需要持续的调用poll
        props.put("session.timeout.ms", "30000");//消费者定时发送心跳的时间间隔
        props.put("max.poll.records", "30000");//每次调用poll拉取消息的最大数量
        props.put("auto.offset.reset", "earliest");//设置分区偏移量无效的情况下，消费者从哪里开始消费
        props.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        //props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "com.bin.kafka.serializer.avro.AvroDeserializer");
        KafkaConsumer<String, User> consumer = new KafkaConsumer<>(props);

        //Kafka Consumer subscribes list of topics here.
        //consumer.subscribe(Arrays.asList(topicName));
        List<TopicPartition> partitions = new ArrayList<>();
        partitions.add(new TopicPartition(topicName, 0));
        partitions.add(new TopicPartition(topicName, 1));
        partitions.add(new TopicPartition(topicName, 2));
        partitions.add(new TopicPartition(topicName, 3));
        consumer.assign(partitions);
        //consumer.seekToBeginning(partitions);
        //print the topic name
        System.out.println("group-haha-Subscribed to topic " + topicName);
        int i = 0;

        while (true) {
            ConsumerRecords<String, User> records = consumer.poll(100);
            for (ConsumerRecord<String, User> record : records) {
                System.out.println("offset = " + record.offset() + " key = " + record.key() + " value = [" + record.value() + "]");
            }
            //consumer.commitSync();设置主动提交offset,手动提交会导致线程阻塞
            //consumer.commitAsync();//异步提交，但是会服务器返回提交失败后不会重试，因为重试会导致多次提交发生互相覆盖的情况
            /*consumer.commitAsync((offsets, e) -> {//可以通过回调的方式记录提交失败的偏移量
                if (e != null)
                    System.out.println("commit error for offsets" + offsets);
            });*/
        }
    }
}

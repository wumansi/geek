package springkafka.demo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.List;
import java.util.Properties;

public class KafkaUtil {

    static KafkaConsumer<String, String> createConsumer(String services, List<String> topic){
        Properties properties = new Properties();
        properties.put("group.id", "java-sisi");
        properties.put("bootstrap.servers", services);
        properties.put("enable.auto.commit", "false");
        properties.put("auto.offset.reset", "earliest");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(topic);
        return consumer;
    }

    static void readMessage(KafkaConsumer<String, String> kafkaConsumer, int timeout){
        ConsumerRecords<String, String> records = kafkaConsumer.poll(timeout);
        for (ConsumerRecord<String, String> record : records){
            String value = record.value();
            kafkaConsumer.commitAsync();
            System.out.println(value);
        }
    }
}

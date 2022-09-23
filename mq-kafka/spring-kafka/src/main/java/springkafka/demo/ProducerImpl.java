package springkafka.demo;


import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ProducerImpl implements Producer {
    private Properties properties;
    private KafkaProducer<String, String> producer;
    private final String topic = "sisi-test1";

    public ProducerImpl(){
        properties = new Properties();
        properties.put("bootstrap.servers","localhost:9001");
        // 发送对应分区批次消息最大大小
        properties.put("batch.size", 16384);
        // 发送消息时间间隔
        properties.put("linger.ms", 1);
        // 生产者内存缓冲区大小
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        // 写入所有分区副本才会发送响应给生产者
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        // 生产者在收到响应前可以发送多少个消息，保证消息按顺序写入服务器
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);
        // 重发消息的次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 1_000);
        // 重试时间间隔，默认100ms
        properties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1_000);
        producer = new KafkaProducer<String, String>(properties);
    }

    @Override
    public void send(Order order) {
        ProducerRecord record = new ProducerRecord(topic, order.getId().toString(), JSON.toJSONString(order));
        Future<RecordMetadata> reslt = producer.send(record, (metadata, exception) -> {
            System.out.println("发送key: " +record.key());
            System.out.println("分区：" + metadata.partition() + ";偏移量：" + metadata.offset());
        });
        try {
            // 确保顺序写入
            reslt.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        producer.close();
    }
}

package springkafka.demo;

import com.alibaba.fastjson.JSON;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ConsumerImpl implements Consumer {
    private Properties properties;
    private KafkaConsumer<String, String> consumer;
    private final String topic = "sisi-test1";
    private Set<String> orderSet = new HashSet<>();
    private Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private volatile boolean flag = true;
    ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>();

    public ConsumerImpl(){
        properties = new Properties();
        properties.put("group.id", "java-sisi");
        properties.put("bootstrap.servers", "127.0.0.1:9001");
        // 手动提交
        properties.put("enable.auto.commit", "false");
        // 当分区有已提交的，则从最大已提交的开始消费，如果没已提交，则从头开始
        properties.put("auto.offset.reset", "earliest");
        properties.put("session.timeout.ms", "30000");
        properties.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<String, String>(properties);
    }

    @Override
    public void consumeOrder() {
        consumer.subscribe(Collections.singletonList(topic));
        try {
            while (true){
                ConsumerRecords<String, String> poll = consumer.poll(Duration.ofSeconds(1));

                for (ConsumerRecord o : poll){
                    ConsumerRecord<String, String> record = (ConsumerRecord) o;
                    Order order = JSON.parseObject(record.value(), Order.class);
                    System.out.println("消费order value = " + record.value());
                    System.out.println("消费order partition = " + record.partition());
                    System.out.println("消费order offset = " + record.offset());
                    deduplicationOrder(order);
                    currentOffsets.put(new TopicPartition(topic, record.partition()),
                            new OffsetAndMetadata(record.offset() + 1, "no metadata"));
                    // 提交offset，下一次要读取的offset
                    consumer.commitAsync(currentOffsets, new OffsetCommitCallback() {
                        @Override
                        public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
                            System.out.println("commit callback map:" + map);
                            if (e != null){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        } catch (CommitFailedException e){
            e.printStackTrace();
        } finally {
            try {
                consumer.commitSync();
            } catch (Exception e){
                consumer.close();
            }
        }

    }

    @Override
    public void close() {
        if (this.flag){
            this.flag = false;
        }
        consumer.close();
    }

    /**
     * ID去重.
     * @param order
     */
    private void deduplicationOrder(Order order) {
        orderSet.add(order.getId().toString());
    }
}

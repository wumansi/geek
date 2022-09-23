package springkafka.demo;

public class KafkaConsumerDemo {
    public static void main(String[] args){
        testConsumer();
    }

    private static void testConsumer() {
        ConsumerImpl consumer = new ConsumerImpl();
        consumer.consumeOrder();
    }
}

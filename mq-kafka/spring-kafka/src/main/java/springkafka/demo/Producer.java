package springkafka.demo;

public interface Producer {
    void send(Order order);

    void close();
}

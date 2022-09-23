package springkafka.demo;

public interface Consumer {
    void consumeOrder();

    void close();
}

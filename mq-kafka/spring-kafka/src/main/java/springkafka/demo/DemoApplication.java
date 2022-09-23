package springkafka.demo;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public NewTopic topic(){
		return TopicBuilder.name("test-topic")
				.partitions(3)
				.replicas(1)
				.build();
	}

	/**
	 * 容器启动后执行.
	 * @param template
	 * @return
	 */
	@Bean
	public ApplicationRunner runner(KafkaTemplate<String, String> template){
		return args -> {
			template.send("topic1","message test");
		};
	}

	@KafkaListener(id = "myId", topics = "topic1")
	public void listen(String in){
		System.out.println("Kafka接收消息：" + in);
	}
}

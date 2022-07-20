package dis_message.activemq_demo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.jms.*;
import java.io.IOException;

@SpringBootTest
class ActivemqDemoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void testMqsend() throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
		Connection connection = factory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		TextMessage message = session.createTextMessage("hello activemq!");
		Queue queue = session.createQueue("test-queue");
		MessageProducer producer = session.createProducer(queue);
		producer.send(message);
		producer.close();
		session.close();
		connection.close();
	}
    @Test
	void testReceive() throws JMSException, IOException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
		Connection connection = factory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = session.createQueue("test-queue");
		MessageConsumer consumer = session.createConsumer(queue);
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				try {
					TextMessage message1 = (TextMessage)message;
					String text = message1.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.in.read();
		consumer.close();
		session.close();
		connection.close();
	}

	@Test
	void testSendTopic() throws JMSException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic destination = session.createTopic("test-topic");
		MessageProducer producer = session.createProducer(destination);
		Message m = session.createTextMessage("test activemq topic");
		producer.send(m);
		producer.close();
		session.close();
		connection.close();
	}

	@Test
	void testTopic() throws JMSException, IOException {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");
		Connection connection = connectionFactory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic destination = session.createTopic("test-topic");
		MessageConsumer consumer = session.createConsumer(destination);
		consumer.setMessageListener(new MessageListener() {
			@Override
			public void onMessage(Message message) {
				TextMessage message1 = (TextMessage) message;
				try {
					String text = message1.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.in.read();
		consumer.close();
		session.close();
		connection.close();
	}

}

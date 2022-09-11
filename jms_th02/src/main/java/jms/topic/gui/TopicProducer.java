package jms.topic.gui;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicProducer implements Runnable{

	private ActiveMQConnectionFactory connectionFactory = null;
	private String messageInput;
	
	public TopicProducer(ActiveMQConnectionFactory connectionFactory, String messageInput) {
		this.connectionFactory = connectionFactory;
		this.messageInput = messageInput;
	}
	
	public void run() {
		try {
			Connection conn = connectionFactory.createConnection();
			conn.start();
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createTopic("KTTKPM_TH02");
			MessageProducer messageProducer = session.createProducer(destination);
			messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
			TextMessage textMessage = session.createTextMessage(messageInput);
			messageProducer.send(textMessage);
			System.out.println("Sent: " + textMessage.getText());
			session.close();
			conn.close();
		} catch (JMSException e) {
			e.printStackTrace();
			System.out.println("Exception: " + e.getMessage());
		}
	}
	
}

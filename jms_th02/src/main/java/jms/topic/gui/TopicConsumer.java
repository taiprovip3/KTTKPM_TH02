package jms.topic.gui;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class TopicConsumer implements Runnable{

	private ActiveMQConnectionFactory connectionFactory = null;
	
	public TopicConsumer(ActiveMQConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	
	public void run() {
		try {
			Connection conn = connectionFactory.createConnection();
			conn.start();
			
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createTopic("KTTKPM_TH02");
			MessageConsumer messageConsumer = session.createConsumer(destination);
			Message message = messageConsumer.receive();
			TextMessage textMessage = (TextMessage) message;
			System.out.println("Receive: " + textMessage.getText());
			session.close();
			conn.close();
		} catch (JMSException e) {
			e.printStackTrace();
			System.out.println("Exception: " + e.getMessage());
		}
		
	}
	
}

package jms.gui.queue;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.swing.JTextField;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Sender extends JFrame {

	private JPanel contentPane;
	private JButton btnSend;
	private JTextField txtMessage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Sender frame = new Sender();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Sender() {
		setTitle("Sender");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtMessage = new JTextField();
		txtMessage.setBounds(10, 11, 414, 32);
		contentPane.add(txtMessage);
		txtMessage.setColumns(10);
		
		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String url = ActiveMQConnection.DEFAULT_BROKER_URL;
				String queueName = "MESSAGE_QUEUE";
				System.out.println("url = " + url);
				try {
					ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
					Connection connection = connectionFactory.createConnection();
					connection.start();
					
					Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
					Destination destination = session.createQueue(queueName);
					MessageProducer producer = session.createProducer(destination);
					String textInput = txtMessage.getText().toString();
					TextMessage message = session.createTextMessage(textInput);
					producer.send(message);
					System.out.println("Message '" + message.getText() + ", Sent Successfully to the Queue");
					connection.close();
					txtMessage.setText("");
				} catch (JMSException e1) {
					e1.printStackTrace();
					System.out.println("Exception: "+e1.getMessage());
				}
			}
		});
		btnSend.setBounds(10, 211, 115, 39);
		contentPane.add(btnSend);
	}
}

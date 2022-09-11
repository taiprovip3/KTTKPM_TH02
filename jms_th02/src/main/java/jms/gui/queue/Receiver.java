package jms.gui.queue;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import javax.swing.JTextArea;

public class Receiver extends JFrame {

	private JPanel contentPane;
	private static JButton btnReload;
	private JButton btnReceive;
	private JTextArea taMessage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					final Receiver frame = new Receiver();
					frame.setVisible(true);
					btnReload.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							SwingUtilities.updateComponentTreeUI(frame);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Receiver() {
		setTitle("Receiver");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnReceive = new JButton("Receive");
		btnReceive.addActionListener(new ActionListener() {
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
					MessageConsumer consumer = session.createConsumer(destination);
					Message message = consumer.receive();
					if (message instanceof TextMessage) {
						TextMessage textMessage = (TextMessage) message;
						System.out.println("Received message '" + textMessage.getText() + "'");
						taMessage.append(textMessage.getText().toString() + "\n");
					}
					connection.close();
				} catch (JMSException e1) {
					e1.printStackTrace();
					System.out.println("Exception: "+ e1.getMessage());
				}
			}
		});
		btnReceive.setForeground(Color.BLUE);
		btnReceive.setBounds(10, 209, 142, 41);
		contentPane.add(btnReceive);
		
		btnReload = new JButton("Reload");
		btnReload.setForeground(Color.RED);
//		btnReload.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				Component component = (Component) e.getSource();
//				Window win = SwingUtilities.getWindowAncestor(component);
//				win.dispose();
//				win.repaint();
//			}
//		});
		btnReload.setBounds(282, 209, 142, 41);
		contentPane.add(btnReload);
		
		taMessage = new JTextArea();
		taMessage.setBounds(10, 11, 414, 187);
		contentPane.add(taMessage);
		
		//Code here
		System.out.println("Receiver starting...");
	}
}

package com.bubbles.learning.jms;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import com.bubbles.learning.converter.DatToJsonConverter;

public class JmsServer {

	private static final String URL = System.getProperty("broker_url", ActiveMQConnection.DEFAULT_BROKER_URL);
	private static final String BROKER_USERNAME = System.getProperty("broker_user_name", "test");
	private static final String BROKER_PASSWORD = System.getProperty("broker_user_password", "test");
	private static String requestDestination = "DatQueue";
	private static String responseDestination = "JsonQueue";
	private static final String SPLIT_BY = "\\|";
	private static final Logger LOGGER = Logger.getLogger(JmsServer.class.getName());

	public static void main(String[] args) {

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(URL);
		Connection connection;
		try {
			connection = connectionFactory.createConnection(BROKER_USERNAME, BROKER_PASSWORD);
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(requestDestination);
			MessageConsumer consumer = session.createConsumer(destination);
			MessageSender messageSender = new MessageSender();

			try {
				while (true) {
					consumer.setMessageListener(
							new ConsumerMessageListener(new MyMessageHandler(messageSender, responseDestination)));
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} finally {
				connection.close();
			}
		} catch (JMSException e) {
			LOGGER.log(Level.SEVERE, "Failed to consume message.", e);
		}

	}

	private static class MyMessageHandler implements MessageHandler {
		private MessageSender messageSender;
		private String destinationName;

		public MyMessageHandler(MessageSender messageSender, String destinationName) {
			super();
			this.messageSender = messageSender;
			this.destinationName = destinationName;
		}

		public void handle(String receivedMessage) throws JMSException {

			List<String> datInput = Arrays.asList(receivedMessage.split(SPLIT_BY));
			String convertedMessage = DatToJsonConverter.convert(datInput);
			messageSender.send(destinationName, convertedMessage);

		}
	}

}

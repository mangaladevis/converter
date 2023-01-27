package com.bubbles.learning.jms;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

public class MessageConsumerRunner implements Runnable {

	private static final String URL = System.getProperty("broker_url", ActiveMQConnection.DEFAULT_BROKER_URL);
	private static final String BROKER_USERNAME = System.getProperty("broker_user_name", "test");
	private static final String BROKER_PASSWORD = System.getProperty("broker_user_password", "test");
	private static final String OUTPUT_DIR = System.getProperty("output-dir", "data/output");
	private static final Logger LOGGER = Logger.getLogger(MessageConsumerRunner.class.getName());

	private String OUTPUT_DESTINATION;

	public MessageConsumerRunner(String OUTPUT_DESTINATION) {
		super();
		this.OUTPUT_DESTINATION = OUTPUT_DESTINATION;
	}

	@Override
	public void run() {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(URL);
		Connection connection;
		try {
			connection = connectionFactory.createConnection(BROKER_USERNAME, BROKER_PASSWORD);
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(OUTPUT_DESTINATION);
			MessageConsumer consumer = session.createConsumer(destination);

			try {
				while (true) {
					consumer.setMessageListener(new ConsumerMessageListener(new MyMessageHandler(OUTPUT_DIR)));
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				LOGGER.log(Level.SEVERE, "Failed to create message listener.", e);
			} finally {
				connection.close();
			}

		} catch (JMSException e) {
			LOGGER.log(Level.SEVERE, "Failed to create connection.", e);
			throw new RuntimeException(e);
		}
	}

	private static class MyMessageHandler implements MessageHandler {

		private static final String FARMATESTYLE = "yyyyMMddHHmmssSSS";
		private static final Logger LOGGER = Logger.getLogger(MyMessageHandler.class.getName());

		private String outputFilepath;

		public MyMessageHandler(String outputFilepath) {
			super();
			this.outputFilepath = outputFilepath;
		}

		public void handle(String receivedMessage) throws JMSException {

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FARMATESTYLE).withZone(ZoneId.of("Z"));
			String timestamp = formatter.format(Instant.now());
			String outputFileName = "Output_" + timestamp + ".json";
			FileWriter file = null;
			try {
				file = new FileWriter(outputFilepath + "/" + outputFileName);
				file.write(receivedMessage);
			} catch (IOException e) {
				String message = "Falied to create the file : " + outputFilepath + "/" + outputFileName + ". ";
				LOGGER.log(Level.SEVERE, message, e);
			} finally {
				if (file != null) {
					try {
						file.close();
					} catch (IOException e) {
						String message = "Falied to close the file : " + outputFilepath + "/" + outputFileName + ". ";
						LOGGER.log(Level.SEVERE, message, e);
					}
				}
			}
		}
	}
}

package com.bubbles.learning.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class MessageSender {
	
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	
	private ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
	private Connection connection = null;
	
	public void send(String destinationName, String message) throws JMSException {

		if (connection == null) {
			connection = connectionFactory.createConnection("test", "test");
			connection.start();
		}
		
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination destination = session.createQueue(destinationName);
		MessageProducer producer = session.createProducer(destination);
		TextMessage txtMessage = session.createTextMessage(message);

		producer.send(txtMessage);
		session.close();
	}
	
}

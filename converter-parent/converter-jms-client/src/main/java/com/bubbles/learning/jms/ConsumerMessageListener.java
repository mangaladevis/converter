package com.bubbles.learning.jms;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ConsumerMessageListener implements MessageListener {
	private MessageHandler handler;
	final Logger LOGGER = Logger.getLogger(ConsumerMessageListener.class.getName());

	public ConsumerMessageListener(MessageHandler handler) {
		this.handler = handler;
	}

	public void onMessage(Message message) {
		TextMessage textMessage = (TextMessage) message;
		try {
			handler.handle(textMessage.getText());
		} catch (JMSException e) {
			LOGGER.log(Level.SEVERE, "Failed while cusuming message.", e);
			throw new RuntimeException(e);
		}
	}
}

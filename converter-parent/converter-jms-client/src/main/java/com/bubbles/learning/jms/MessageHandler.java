package com.bubbles.learning.jms;

import javax.jms.JMSException;

public interface MessageHandler {
	
	void handle(String receivedMessage) throws JMSException;
	
}

package ru.obelisk.monitor.jms;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

@Component
public class JmsMessageProducer {
 
	private static final Logger logger = LogManager.getLogger(JmsMessageProducer.class);
	@Autowired
	private JmsTemplate template;
	private int messageCount = 10;
 
	/**
	* Generates JMS messages
	 * @throws InterruptedException 
	*/
	public void generateMessages() throws JMSException, InterruptedException {
		for (int i = 0; i < messageCount; i++) {
			final int index = i;
			final String text = "Message number is " + i + ".";
 
			template.send(new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					TextMessage message = session.createTextMessage(text);
					message.setIntProperty("messageCount", index);
					logger.info("Sending message: " + text);
					return message;
				}
			});
		}
	}
	
	public void sendMessage(String destination, final Serializable msg){
		try { 
			template.send(destination, new MessageCreator() {
				public Message createMessage(Session session) throws JMSException {
					ObjectMessage message = session.createObjectMessage(msg);
					logger.info("Sending message: " + message);
					return message;
				}
			});
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void setDestination(String destinationName){
		template.setDefaultDestinationName(destinationName);
    }
	
	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}
 
	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}
}

package ru.obelisk.monitor.jms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.TextMessage;
 
@Component
public class JmsMessageListener implements MessageListener {
 
	
	private static final Logger logger = LogManager.getLogger(JmsMessageListener.class);
	@Autowired HandleRecivedObject handleObject;
	
	/**
	* Implementation of <code>MessageListener</code>.
	*/
	@JmsListener(destination = "obelisk.head")
	public void onMessage(Message message) {
		try {
			if (message instanceof TextMessage) {
				TextMessage tm = (TextMessage) message;
				String msg = tm.getText();
				logger.info("Processed message '{}'", msg);
			}
			if (message instanceof ObjectMessage) {
				ObjectMessage recivedMessage = (ObjectMessage) message;
				Object recivedObject = recivedMessage.getObject();
				if(recivedObject!=null)
					handleObject.handleObject(recivedObject);
			}
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}
}











package ru.obelisk.monitor.web.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import ru.obelisk.message.Content;
import ru.obelisk.message.Header;
import ru.obelisk.message.MessageData;
import ru.obelisk.message.ObeliskMessage;
import ru.obelisk.message.commands.Command;
import ru.obelisk.message.commands.CommandRequest;
import ru.obelisk.message.commands.HangupRequest;
import ru.obelisk.message.commands.OriginateRequest;
import ru.obelisk.message.commands.SipNotifyRequest;
import ru.obelisk.message.data.OriginateData;
import ru.obelisk.monitor.jms.JmsMessageProducer;

public class WebsocketEndPoint extends TextWebSocketHandler {
	@Autowired
	private JmsMessageProducer producer;
	private WebSocketSession session=null;
	private static final Logger logger = LogManager.getLogger(WebsocketEndPoint.class);
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		this.setSession(session);
	}
	
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		this.setSession(null);
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session,
			TextMessage message) throws Exception {
		super.handleTextMessage(session, message);
		
		
		TextMessage returnMessage = new TextMessage(message.getPayload());//+" received at server");
		logger.info(returnMessage.getPayload());
				
		Command command = new OriginateRequest();
		MessageData data = new MessageData();
		data.addDataElement(new OriginateData());
				
		Content content = new Content();
		content.setCommand(command);
		content.setMessageData(data);

		Header header = new Header();
		header.setFrom("FROM");
		header.setTo("TO");
		
		ObeliskMessage newMess = new ObeliskMessage(header, content); 
				
		ObjectMapper mapper2 = new ObjectMapper();
		mapper2.writeValueAsString(newMess);
		logger.info("NewMess: {}",mapper2.writeValueAsString(newMess));
				
		ObjectMapper mapper = new ObjectMapper();
	    try {
	    	ObeliskMessage recivedMess =  mapper.readValue(returnMessage.getPayload(), ObeliskMessage.class);
			
			if(recivedMess.getContent().getCommand() instanceof HangupRequest){
		    	MessageData mesData = recivedMess.getContent().getMessageData();
		    	
		    	//Channel chan = (Channel) mesData.getArgs().get(0);
		    	
		    	logger.info("Hangup Request on channel {}\r\n", mesData);
		    	//logger.info("producer.sendMessage({},{})\r\n", chan.getServer(), recivedMess);
		    	logger.info("producer.sendMessage({},{})\r\n", recivedMess.getHeader().getTo(), recivedMess);
		    	producer.sendMessage(recivedMess.getHeader().getTo(), recivedMess);
		    	//producer.sendMessage(chan.getServer(), recivedMess);
		    } else if(recivedMess.getContent().getCommand() instanceof OriginateRequest){
		    	logger.info("Originate Request on server {}: {}\r\n", recivedMess.getHeader().getTo(), recivedMess);
		    	
		    	/*MessageData mesData = recivedMess.getContent().getMessageData();
		    	OriginateData origData = (OriginateData) mesData.getArgs().get(0);
		    	
		    	origData.getChannel();*/
		    	
		    	//producer.sendMessage("msk-tl-001.corp.rostbank.ru", recivedMess);
		    	producer.sendMessage(recivedMess.getHeader().getTo(), recivedMess);
		    	
		    	
		    	/*MessageData mesData = recivedMess.getContent().getMessageData();
		    	logger.info("Hangup Request on channel {}\r\n", mesData);
		    	Channel chan = (Channel)mesData.getArgs().get(0);
		    	logger.info("producer.sendMessage({},{})\r\n", chan.getServer(), recivedMess);
		    	producer.sendMessage(chan.getServer(), recivedMess);*/
		    } else if(recivedMess.getContent().getCommand() instanceof SipNotifyRequest){
		    	logger.info("SipNotify Request on server {}: {}\r\n",recivedMess.getHeader().getTo(), recivedMess);
		    	producer.sendMessage(recivedMess.getHeader().getTo(), recivedMess);
		    } else if(recivedMess.getContent().getCommand() instanceof CommandRequest){
		    	logger.info("Command Request on server {}: {}\r\n",recivedMess.getHeader().getTo(), recivedMess);
		    	producer.sendMessage(recivedMess.getHeader().getTo(), recivedMess);
		    } else {
		    	logger.info("Class Type: {}\r\n", recivedMess.getContent().getCommand().getClass());
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public WebSocketSession getSession() {
		return session;
	}

	public void setSession(WebSocketSession session) {
		this.session = session;
	}
}
package ru.obelisk.monitor.config;

import java.util.Arrays;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.security.AuthenticationUser;
import org.apache.activemq.security.SimpleAuthenticationPlugin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.connection.SingleConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.apache.activemq.hooks.SpringContextHook;

@Configuration
@EnableJms
public class JmsConfig {
		
	@Value("${broker.url}")	private String broker_url;
	@Value("${broker.username}") private String username; 
	@Value("${broker.password}") private String password;
	@Value("${broker.authentication.enabled}") private boolean authenticationEnabled; 
	
	@Bean
	public BrokerService brokerService() throws Exception {
		final BrokerService rv = BrokerFactory.createBroker(
			String.format("broker:("
				+ "vm://localhost,"
				/*+ "tcp://%s:%d,"*/
				+ "%s"
				+ ")?persistent=false&useJmx=%s&useShutdownHook=true",
				broker_url,
				false
			)
		);

		
		if(authenticationEnabled){
			final SimpleAuthenticationPlugin authenticationPlugin = new SimpleAuthenticationPlugin();
			authenticationPlugin.setAnonymousAccessAllowed(false);
			authenticationPlugin.setUsers(Arrays.asList(new AuthenticationUser(username, password, "")));
			rv.setPlugins(new BrokerPlugin[]{authenticationPlugin});
		}
		rv.addShutdownHook(new SpringContextHook());
		rv.start();
		return rv;
	}
	
	public ActiveMQConnectionFactory jmsFactoryVm(){
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
		factory.setBrokerURL("vm://localhost");
		return factory;
	}
	
	@Bean
	public SingleConnectionFactory jmsConsumerConnectionFactory(){
		SingleConnectionFactory conn = new SingleConnectionFactory();
		conn.setTargetConnectionFactory(jmsFactoryVm());
		conn.setReconnectOnException(true);
		return conn;
	}
	
	@Bean
	public JmsTemplate jmsProducerTemplate(){
		JmsTemplate template = new JmsTemplate();
		template.setConnectionFactory(jmsConsumerConnectionFactory());
		template.setDeliveryPersistent(true);
		return template;
	}

}

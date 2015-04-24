package ru.obelisk.monitor.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration 
@ComponentScan(basePackages = {"ru.obelisk.*"})
@EnableWebMvc
@SpringBootApplication(exclude=JmsAutoConfiguration.class )
@Import({SpringConfig.class,  JmsConfig.class, ThymeleafConfig.class, WebSocketConfig.class, DataConfig.class})
public class AppConfig extends WebMvcConfigurerAdapter {
    
	//Maps resources path to webapp/resources
	  @Override
	  public void addResourceHandlers(ResourceHandlerRegistry registry) {
		  //registry.addResourceHandler("/static/assets/**").addResourceLocations("classpath:/static/assets/");
		  registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/"); 
		  
		  
	  }
	  
	
	  //Only needed if we are using @Value and ${...} when referencing properties
	/*  @Bean
	  public static PropertySourcesPlaceholderConfigurer properties() {
		  PropertySourcesPlaceholderConfigurer propertySources = new PropertySourcesPlaceholderConfigurer();
		  Resource[] resources = new ClassPathResource[] { 
				  new ClassPathResource("application.properties") };
		  propertySources.setLocations(resources);
		  propertySources.setIgnoreUnresolvablePlaceholders(true);
		  return propertySources;
	  }*/
	
	  //Provides internationalization of messages
	  @Bean
	  public ResourceBundleMessageSource messageSource() {
		  ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		  source.setBasename("messages");
		  return source;
	  }
}


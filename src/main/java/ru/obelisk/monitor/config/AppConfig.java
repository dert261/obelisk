package ru.obelisk.monitor.config;

import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import ru.obelisk.monitor.web.databinding.DatatableCriteriasHandlerMethodArgumentResolver;

@Configuration 
@ComponentScan (basePackages = {"ru.obelisk.*"})
@EnableWebMvc
@EnableAutoConfiguration
@SpringBootApplication(exclude=JmsAutoConfiguration.class )
@Import({SpringConfig.class,  JmsConfig.class, ThymeleafConfig.class, WebSocketConfig.class})
public class AppConfig extends WebMvcConfigurerAdapter {
    
	//Maps resources path to webapp/resources
	
	  @Override
	  public void addResourceHandlers(ResourceHandlerRegistry registry) {
		  if (!registry.hasMappingForPattern("/static/**")) {
		        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		  }
	  }
	  
	  @Bean
	  public FilterRegistrationBean hiddenFilterRegistrationBean() {
		  return new FilterRegistrationBean(new HiddenHttpMethodFilter());
	  }
	  
	  @Override
	  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		  argumentResolvers.add(new DatatableCriteriasHandlerMethodArgumentResolver());
	      // equivalent to <mvc:argument-resolvers>
	  }

	 	  	 
	  /*@Bean
	  public EmbeddedServletContainerCustomizer containerCustomizer(){
	      return new MyCustomizer();
	  }*/
	  
	 	  
	 /* @Bean
	  public FilterRegistrationBean hiddenFilterRegistrationBean() {
		  return new FilterRegistrationBean(new HiddenHttpMethodFilter());
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
/*	  @Bean
	  public ResourceBundleMessageSource i18nMessageSource() {
		  ResourceBundleMessageSource source = new ResourceBundleMessageSource();
		  source.setBasename("messages");
		  return source;
	  }
*/	  
	  	@Bean
	    public MessageSource messageSource() {
	        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	        messageSource.setBasename("messages");
	        return messageSource;
	    }
}


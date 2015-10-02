package ru.obelisk.monitor.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import ru.obelisk.monitor.web.databinding.DatatableCriteriasHandlerMethodArgumentResolver;

@Configuration 
@ComponentScan (basePackages = {"ru.obelisk.*"})
@EnableWebMvc
@EnableAutoConfiguration
@SpringBootApplication(exclude=JmsAutoConfiguration.class )
@EnableTransactionManagement
@EnableCaching
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("ru.obelisk.monitor.database.models.repository")
@Import({SpringConfig.class,  JmsConfig.class, ThymeleafConfig.class, WebSocketConfig.class})
public class AppConfig extends WebMvcConfigurerAdapter {
      
	//Maps resources path to webapp/resources
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (!registry.hasMappingForPattern("/static/**")) {
			registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		}
	}
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new DatatableCriteriasHandlerMethodArgumentResolver());
		// equivalent to <mvc:argument-resolvers>
	}
	
	/**
	    *  Total customization - see below for explanation.
	    */
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
			
		configurer.favorPathExtension(true).
	    	favorParameter(false).
	        //parameterName("mediaType").
	        ignoreAcceptHeader(true).
	        useJaf(false).
	        defaultContentType(MediaType.APPLICATION_JSON).
	        mediaType("xml", MediaType.APPLICATION_XML).
	        mediaType("json", MediaType.APPLICATION_JSON).
	        mediaType("html", MediaType.TEXT_HTML);
	}
	
	@Autowired
	private ThymeleafConfig thymeleafConfig;
	
	@Bean
	public ViewResolver contentNegotiatingViewResolver(ContentNegotiationManager manager) {
	    // Define the view resolvers
	    List<ViewResolver> resolvers = new ArrayList<ViewResolver>();
	    resolvers.add(thymeleafConfig.thymeleafViewResolver());
	    ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
	    resolver.setViewResolvers(resolvers);
	    resolver.setContentNegotiationManager(manager);
	    return resolver;
	}  
}


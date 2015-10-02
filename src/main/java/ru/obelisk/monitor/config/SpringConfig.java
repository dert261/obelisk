package ru.obelisk.monitor.config;

import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import ru.obelisk.message.data.HostInfoListImpl;

@Configuration
public class SpringConfig{
	
	@Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }
	
	@Bean
	public FilterRegistrationBean hiddenFilterRegistrationBean() {
		return new FilterRegistrationBean(new HiddenHttpMethodFilter());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder () {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
	    return encoder;
	}
	  
	@Bean
	public javax.validation.Validator localValidatorFactoryBean() {
		return new LocalValidatorFactoryBean();
	}
	  
	@Bean
	public Jackson2ObjectMapperBuilder jacksonBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.indentOutput(true).dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		return builder;
	}
	  
	@Bean
	public SecurityInterceptor securityInterceptor(){
		return new SecurityInterceptor();
	}
		
	@Autowired
	private DataConfig dataConfig;
	  
	@Bean
	public RequestMappingHandlerMapping requestMappingHandlerMapping(){
		Object[] interceptors = {securityInterceptor(),dataConfig.openEntityManagerInViewInterceptor()};
		RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
		mapping.setInterceptors(interceptors);
		return mapping;
	}
	  
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
	    messageSource.setBasename("messages");
	    return messageSource;
	}
		
	@Bean
	public HostInfoListImpl hostInfoList(){
		return new HostInfoListImpl();
	}
	
	  		
}


/*
	
	

*/
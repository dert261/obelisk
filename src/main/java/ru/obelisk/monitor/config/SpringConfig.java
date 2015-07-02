package ru.obelisk.monitor.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;


import ru.obelisk.message.data.HostInfoListImpl;
/*import ru.obelisk.message.data.PeersListImpl;
import ru.obelisk.message.data.ChannelsListImpl;*/
import ru.obelisk.monitor.web.utils.conversion.DateFormatter;

@Configuration
public class SpringConfig {
	
	
	@Bean 
	public FormattingConversionServiceFactoryBean conversionService() {
		
		FormattingConversionServiceFactoryBean convertor = new FormattingConversionServiceFactoryBean();
		Set<Formatter<?>> dateFormater=new HashSet<Formatter<?>>();
		dateFormater.add(new DateFormatter());
		convertor.setFormatters(dateFormater);
	    return convertor;
	}
	
	
	
/*	@Bean
	public StringHttpMessageConverter httpMessageConverter(){
		StringHttpMessageConverter messConverter = new StringHttpMessageConverter();
		messConverter.setSupportedMediaTypes(Arrays.asList(new MediaType("text", "plain", Charset.forName("UTF-8"))));
		return messConverter;
	}
	
	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter(){
		RequestMappingHandlerAdapter req = new RequestMappingHandlerAdapter();
		List<HttpMessageConverter<?>> messageConverters=new ArrayList<HttpMessageConverter<?>>();
	    messageConverters.add(httpMessageConverter());
	    req.setMessageConverters(messageConverters);
	    req.setCacheSeconds(0);
	    return req;
	}
	*/
	@SuppressWarnings("deprecation")
	@Bean
	public ContentNegotiatingViewResolver contentNegotiatingResolver(){
		ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
		Map<String,String> mediaTypes = new HashMap<String, String>();
				
		mediaTypes.put("html","text/html");
		mediaTypes.put("pdf","application/pdf");
		mediaTypes.put("xsl","application/vnd.ms-excel");
		mediaTypes.put("xml","application/xml");
		mediaTypes.put("json","application/json");
		resolver.setMediaTypes(mediaTypes);		
		return resolver;
	}
	
	/*@Bean
	public ChannelsListImpl channelsList(){
		return new ChannelsListImpl();
	}
	
	@Bean
	public PeersListImpl peersList(){
		return new PeersListImpl();
	}*/
	
	@Bean
	public HostInfoListImpl hostInfoList(){
		return new HostInfoListImpl();
	}
	
	  		
}


/*
	
	

*/
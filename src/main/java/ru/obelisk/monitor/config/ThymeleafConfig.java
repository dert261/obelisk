package ru.obelisk.monitor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@Configuration 
public class ThymeleafConfig {

	@Bean 
	public ServletContextTemplateResolver templateResolver() {
	    ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
	    resolver.setPrefix("classpath:/templates/views/");
	    resolver.setSuffix(".html");
	    resolver.setTemplateMode("HTML5");
	    resolver.setCacheable(false); //Must true in prod-mod
	    resolver.setCharacterEncoding("UTF-8");
	    resolver.setOrder(1);
	    return resolver;
	}
	
	@Bean 
	public TemplateEngine templateEngine() {
	    SpringTemplateEngine engine = new SpringTemplateEngine();
	    engine.setTemplateResolver(templateResolver());
	    //engine.addDialect(new DandelionDialect());
	    //engine.addDialect(new DataTablesDialect());
	    //engine.addDialect(new SpringSecurityDialect());
	    //engine.addDialect(new SpringStandardDialect());
	    //engine.addDialect(new Html5ValDialect());
	    return engine;
	}
	
	@Bean 
	public ThymeleafViewResolver thymeleafViewResolver() {
	    final ThymeleafViewResolver resolver = new ThymeleafViewResolver();
	    resolver.setTemplateEngine((SpringTemplateEngine) templateEngine());
	    resolver.setCharacterEncoding("UTF-8");
	    resolver.setOrder(1);
	    return resolver;
	}
}
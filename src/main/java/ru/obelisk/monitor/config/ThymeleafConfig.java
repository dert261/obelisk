package ru.obelisk.monitor.config;


//import javax.annotation.PostConstruct;

//import net.sourceforge.html5val.Html5ValDialect;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect;
//import org.thymeleaf.spring4.SpringTemplateEngine;
//import org.thymeleaf.spring4.dialect.SpringStandardDialect;
//import org.thymeleaf.spring4.view.ThymeleafViewResolver;
//import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


@Configuration 
public class ThymeleafConfig {

	/*@Autowired
    private SpringTemplateEngine templateEngine;
	
/*	@PostConstruct
    public void extension() {
		templateEngine.addDialect(new Html5ValDialect());
	}

/*	@PostConstruct
    public void extension() {
        /*FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("D:\\templates\\");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(templateEngine.getTemplateResolvers().size());
        resolver.setCacheable(false);*/
  
/*		TemplateResolver resolver = new TemplateResolver();
	    //resolver.setResourceResolver(resourceResolver);
	    resolver.setPrefix("classpath:/templates/views/");
	    resolver.setSuffix(".html");
	    resolver.setTemplateMode("HTML5");
	    resolver.setCharacterEncoding("UTF-8");
	    resolver.setCacheable(false);
	    resolver.setOrder(templateEngine.getTemplateResolvers().size());
        templateEngine.addTemplateResolver(resolver);
	}
  */  
	
	/*@Bean 
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
	public SpringTemplateEngine templateEngine() {
	    SpringTemplateEngine engine = new SpringTemplateEngine();
	    engine.setTemplateResolver(templateResolver());
	    //engine.addDialect(new SpringSecurityDialect());
	    engine.addDialect(new SpringStandardDialect());
	    engine.addDialect(new Html5ValDialect());
	    return engine;
	}
	
	@Bean 
	public ThymeleafViewResolver thymeleafViewResolver() {
	    final ThymeleafViewResolver resolver = new ThymeleafViewResolver();
	    resolver.setTemplateEngine(templateEngine());
	    resolver.setCharacterEncoding("UTF-8");
	    resolver.setOrder(1);
	    return resolver;
	} */
 
}
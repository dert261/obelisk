package ru.obelisk.monitor.config;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

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
	
	@Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }
	
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
	  
	  
	  /*@Override
	  public void addInterceptors(InterceptorRegistry registry) {
		  registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor());
	  }*/
	  
	  @Bean
		public SecurityInterceptor securityInterceptor(){
			return new SecurityInterceptor();
		}
		
		@Bean
		public RequestMappingHandlerMapping requestMappingHandlerMapping(){
			Object[] interceptors = {securityInterceptor(),openEntityManagerInViewInterceptor()};
			RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
			mapping.setInterceptors(interceptors);
			return mapping;
		}
	  
	  /* 
	  
	  public void addInterceptors(InterceptorRegistry registry) {
		  OpenSessionInViewInterceptor openSessionInterceptor = new OpenSessionInViewInterceptor();
		  openSessionInterceptor.setSessionFactory(sessionFactory);
		  registry.addWebRequestInterceptor(openEntityManagerInViewInterceptor());
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
	  	
	  	
//----------------------------------------DATA CONFIG ------------------------------------------------------//
	  	
	  	private static final String PROP_DATABASE_DRIVER = "db.driver";
	    private static final String PROP_DATABASE_PASSWORD = "db.password";
	    private static final String PROP_DATABASE_URL = "db.url";
	    private static final String PROP_DATABASE_USERNAME = "db.username";
	    private static final String PROP_HIBERNATE_DIALECT = "hibernate.dialect";
	    private static final String PROP_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
	    private static final String PROP_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";
	    private static final String PROP_HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
	    
	    private static final String PROP_HIBERNATE_CACHE_REGION_FACTORYCLASS = "hibernate.cache.region.factory_class";
	    private static final String PROP_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE = "hibernate.cache.use_second_level_cache";
	    private static final String PROP_HIBERNATE_CACHE_USE_QUERY_CACHE = "hibernate.cache.use_query_cache";
	    
	   	    		
	    @Resource
	    private Environment env;
	 
	    @Bean
	    public DataSource dataSource() {
	        DriverManagerDataSource dataSource = new DriverManagerDataSource();
	        dataSource.setDriverClassName(env.getRequiredProperty(PROP_DATABASE_DRIVER));
	        dataSource.setUrl(env.getRequiredProperty(PROP_DATABASE_URL));
	        dataSource.setUsername(env.getRequiredProperty(PROP_DATABASE_USERNAME));
	        dataSource.setPassword(env.getRequiredProperty(PROP_DATABASE_PASSWORD));
	        return dataSource;
	    }
			    
	    @Bean
		public OpenEntityManagerInViewInterceptor openEntityManagerInViewInterceptor() {
	    	OpenEntityManagerInViewInterceptor interceptor = new OpenEntityManagerInViewInterceptor();
			interceptor.setEntityManagerFactory(entityManagerFactory().getObject());
			return interceptor; 
		}
	    	    
	    @Bean
	    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
	        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
	        entityManagerFactoryBean.setDataSource(dataSource());
	        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
	        entityManagerFactoryBean.setPackagesToScan(env.getRequiredProperty(PROP_ENTITYMANAGER_PACKAGES_TO_SCAN));
	 
	        entityManagerFactoryBean.setJpaProperties(getHibernateProperties());
	 
	        return entityManagerFactoryBean;
	    }
	    
	    @Bean
	    public JpaTransactionManager transactionManager() {
	        JpaTransactionManager transactionManager = new JpaTransactionManager();
	        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
	        return transactionManager;
	    }
	 
	    private Properties getHibernateProperties() {
	        Properties properties = new Properties();
	        properties.put(PROP_HIBERNATE_DIALECT, env.getRequiredProperty(PROP_HIBERNATE_DIALECT));
	        properties.put(PROP_HIBERNATE_SHOW_SQL, env.getRequiredProperty(PROP_HIBERNATE_SHOW_SQL));
	        properties.put(PROP_HIBERNATE_HBM2DDL_AUTO, env.getRequiredProperty(PROP_HIBERNATE_HBM2DDL_AUTO));
	       
	        properties.put(PROP_HIBERNATE_CACHE_REGION_FACTORYCLASS, env.getRequiredProperty(PROP_HIBERNATE_CACHE_REGION_FACTORYCLASS));
	        properties.put(PROP_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE, env.getRequiredProperty(PROP_HIBERNATE_CACHE_USE_SECOND_LEVEL_CACHE));
	        properties.put(PROP_HIBERNATE_CACHE_USE_QUERY_CACHE, env.getRequiredProperty(PROP_HIBERNATE_CACHE_USE_QUERY_CACHE));
	        return properties;
	    }
	 
	  	
}


package ru.obelisk.monitor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true )
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	
	@Autowired
    private UserDetailsService customUserDetailsService;
	
	@Autowired 
	private PasswordEncoder passwordEncoder;

	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //PasswordCrypto.getInstance();
		auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }
	
	/*@Override
	@Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("user1").password("user1").roles("ADMIN");/*.and()
                .withUser("admin").password("password").roles("USER", "ADMIN");*/
  /*  }
*/
    
	
	

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
	    	.ignoring()
	    		.antMatchers("/static/**")
	    		.antMatchers("/resources/**")
	        	.antMatchers("/assets/**")
	        	.antMatchers("/dandelion/**")
	        	.antMatchers("/dandelion-assets/**")
	        	.antMatchers("/css/**")
	        	.antMatchers("/webjars/**")
	        	.antMatchers("/images/**")
	        	.antMatchers("/error*")
	        	.antMatchers("/login*")
	        	.antMatchers("/logout*")
	        	.antMatchers("/favicon.ico");
		
		
		
	}
	

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.csrf().disable()
            .authorizeRequests()
            	.antMatchers("/**").hasRole("ADMIN")
            	.anyRequest().authenticated()
            	.and()
            .formLogin()
                .loginPage("/login")
                .failureUrl("/login?failed")
                .loginProcessingUrl("/authentication")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
            .rememberMe()
            	.tokenValiditySeconds(1209600)
            	.key("remember-me");
            	
    }
}


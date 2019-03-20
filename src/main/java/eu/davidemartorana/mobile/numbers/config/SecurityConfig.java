package eu.davidemartorana.mobile.numbers.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * 
 * @author davidemartorana
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.cors().and().csrf().disable()
	    .authorizeRequests()
	        .requestMatchers(EndpointRequest.toAnyEndpoint()).authenticated()
	    .and()
	    	.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    .and()
	        .httpBasic();
		
	}
	
}

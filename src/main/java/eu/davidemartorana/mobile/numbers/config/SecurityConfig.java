package eu.davidemartorana.mobile.numbers.config;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 
 * @author davidemartorana
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


 	
// 	@Override
// 	protected void configure(HttpSecurity http) throws Exception {
// 		http.authorizeRequests().antMatchers("/public/**").permitAll().anyRequest()
// 				.hasRole("USER").and()
// 				// Possibly more configuration ...
// 				.formLogin() // enable form based log in
// 				// set permitAll for all URLs associated with Form Login
// 				.permitAll();
// 	}

// 	@Override
// 	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
// 		auth
// 		// enable in memory based authentication with a user named "user" and "admin"
// 		.inMemoryAuthentication().withUser("user").password("password").roles("USER")
// 				.and().withUser("admin").password("password").roles("USER", "ADMIN");
// 	}

 	// Possibly more overridden methods ...
	
	
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
	    .authorizeRequests()
	        // .requestMatchers(EndpointRequest.to("env")).permitAll()
	        // .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
	        // .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
	        // .antMatchers("/**").hasRole("USER")
	        .requestMatchers(EndpointRequest.toAnyEndpoint()).authenticated()
	    .and()
	        .httpBasic();
	}
	
}

package com.curso.ecomerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringBootSecurity {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public UserDetailsService userDetailsService(){
		return userDetailsService;
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.requestMatchers("/administrador/**").hasRole("ADMIN")
				.requestMatchers("/productos/**").hasRole("ADMIN")
				.and().formLogin().loginPage("/usuario/login")
				.permitAll().defaultSuccessUrl("/usuario/acceder");
		return http.build();
	}
	
	@Bean
	public BCryptPasswordEncoder getEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}

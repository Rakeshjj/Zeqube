package com.restapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationConverter jwtAuthenticationConverter;

	public SecurityConfig(JwtAuthenticationConverter jwtAuthenticationConverter) {
		super();
		this.jwtAuthenticationConverter = jwtAuthenticationConverter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(http -> http.anyRequest().authenticated()).oauth2ResourceServer(oauth -> {
					oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter));
				}).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();

	}
}

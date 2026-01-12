package com.ecommerce.payment.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import jwtcommon.security.JwtAuthFilter;

@Configuration
@EnableMethodSecurity
public class PaymentSecurityConfig {
	private JwtAuthFilter authFilter;
	public PaymentSecurityConfig(JwtAuthFilter authFilter) {
		this.authFilter=authFilter;
	}
	
	@Bean
	public SecurityFilterChain paymentSecurityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf->csrf.disable())
			.cors(cors->cors.configurationSource(request->{
				CorsConfiguration config=new CorsConfiguration();
				config.setAllowCredentials(true);
				config.setAllowedHeaders(List.of("*"));
				config.setExposedHeaders(List.of("Set-Cookie"));
				config.setAllowedMethods(List.of("POST","GET","PUT","PATCH","DELETE","OPTIONS"));
				config.setAllowedOriginPatterns(List.of("http://localhost:4200"));
				return config;
			}))
			.authorizeHttpRequests(auth->auth
				.requestMatchers("/payments/**").hasRole("USER")
				.anyRequest().authenticated()
			)
			.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
			.sessionManagement(sess->sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
	}
}

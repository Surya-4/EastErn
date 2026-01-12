package com.ecommerce.cart.config;

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
public class CartSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public CartSecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
        	.cors(cors->cors.configurationSource(request->{
        		CorsConfiguration config=new CorsConfiguration();
        		config.setAllowCredentials(true);
        		config.setAllowedOriginPatterns(List.of("http://localhost:4200"));
        		config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        		config.setAllowedHeaders(List.of("*"));
        		config.setExposedHeaders(List.of("Set-Cookie"));
        		return config;
        	}))
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers("/cart/checkout").permitAll()
                .requestMatchers("/cart/**").hasRole("USER")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

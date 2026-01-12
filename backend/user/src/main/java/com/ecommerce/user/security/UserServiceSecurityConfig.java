package com.ecommerce.user.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.ecommerce.service.CustomeUserDetailsService; // your service-specific class
import jwtcommon.security.JwtAuthFilter; // from common library

@Configuration
@EnableMethodSecurity
public class UserServiceSecurityConfig {

    private final CustomeUserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;

    public UserServiceSecurityConfig(CustomeUserDetailsService userDetailsService,
                                     JwtAuthFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
        	.cors(cors->cors.configurationSource(request->{
        		CorsConfiguration corsConfig=new CorsConfiguration();
        		corsConfig.setAllowCredentials(true);
        		corsConfig.setAllowedOriginPatterns(List.of("http://localhost:4200"));
        		corsConfig.setAllowedHeaders(List.of("*"));
        		corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        	    corsConfig.setExposedHeaders(List.of("Set-Cookie"));
        		return corsConfig;
        	}))
            .authorizeHttpRequests(auth -> auth
            	    .requestMatchers("/users/login", "/users/register","/users/logout").permitAll()
            	    .requestMatchers("/users/me").hasRole("USER")
            	    .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

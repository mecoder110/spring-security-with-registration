package com.ali.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurity {
	private static final String[] WHITE_LIST_URLS = { "v1/register", "/health", "v1/registrationVarify*", "v1/*", "v1/regenerateToken/*","v1/resetPwdToken/*" };

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf->csrf.disable())
                .authorizeHttpRequests()
                .requestMatchers(WHITE_LIST_URLS).permitAll()
				/*
				 * .and()
				 * .authorizeHttpRequests().requestMatchers("/auth/user/**").authenticated()
				 * .and()
				 * .authorizeHttpRequests().requestMatchers("/auth/admin/**").authenticated()
				 */.and().formLogin()
                .and().build();
    }

	/*
	 * public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	 * http .cors() .and() .csrf() .disable().authorizeHttpRequests().p
	 * 
	 * .requestMatchers(WHITE_LIST_URLS).permitAll(); return http.build();
	 * 
	 * }
	 */
}

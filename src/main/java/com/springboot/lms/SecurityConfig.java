package com.springboot.lms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //<- This ensures that this class gets called during every API call
public class SecurityConfig {
	@Autowired
	private JwtFilter jwtFilter;
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf((csrf) -> csrf.disable())
				.cors(Customizer.withDefaults())
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/api/learner/get-all").permitAll()
						.requestMatchers("/api/user/signup").permitAll()
						.requestMatchers("/api/user/login").permitAll()
						.requestMatchers("/api/user/details").permitAll()
						.requestMatchers("/api/user/token").permitAll()
						.requestMatchers("/api/learner/add").permitAll()
						.requestMatchers("/api/course/all").permitAll()
						.requestMatchers("/api/course/by-author").hasAuthority("AUTHOR")
						.requestMatchers("/api/learner/get-one").hasAuthority("LEARNER")
						.requestMatchers("/api/course/add").hasAnyAuthority("AUTHOR","EXECUTIVE")
						.anyRequest().authenticated()
				)
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.httpBasic(Customizer.withDefaults()); //<- this activated http basic technique
		return http.build();
	}

		@Bean
		PasswordEncoder passwordEncoder() {  //<- Bean saves this object in spring's context
			return new BCryptPasswordEncoder();
		}

		@Bean
		AuthenticationManager getAuthManager(AuthenticationConfiguration auth)
				throws Exception {
			return auth.getAuthenticationManager();
		}
}
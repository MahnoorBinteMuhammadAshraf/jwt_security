package com.jwt.security;

import com.jwt.filter.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration{

    private final JWTAuthorizationFilter jwtAuthorizationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Autowired
    public SecurityConfiguration(JWTAuthorizationFilter jwtAuthorizationFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       return http.authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/manager/**").hasRole("MANAGER")
                        .anyRequest().authenticated())
               .authenticationProvider(authenticationProvider)
               .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
               .csrf(AbstractHttpConfigurer::disable)
               .sessionManagement(sessionManagement ->  sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .build();
    }
}


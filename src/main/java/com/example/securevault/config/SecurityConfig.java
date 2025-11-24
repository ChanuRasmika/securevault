package com.example.securevault.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
            ObjectProvider<ClientRegistrationRepository> clientRegistrationRepository) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/", "/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated()
            );


        if (clientRegistrationRepository.getIfAvailable() != null) {
            http.oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/dashboard", true)
            );
        }

        http.logout(logout -> logout
            .logoutSuccessUrl("/")
        );

        return http.build();
    }
}
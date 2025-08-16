package com.thekade.nopolin.appointment_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/lawyers").permitAll()
                        .requestMatchers("/api/lawyers/specialization/**").permitAll()
                        .requestMatchers("/api/lawyers/city/**").permitAll()
                        .requestMatchers("/api/lawyers/search").permitAll()
                        .requestMatchers("/api/lawyers/free-consultation").permitAll()
                        .requestMatchers("/api/lawyers/{id}").permitAll()
                        .requestMatchers("/api/court-cases/case-number/**").permitAll()
                        .requestMatchers("/api/appointments/{id}").permitAll()
                        .requestMatchers("/api/reschedule-requests/{id}").permitAll()
                        .requestMatchers("/appointments/test").authenticated()
                        // Admin/Gov Officer endpoints
                        .requestMatchers("/api/lawyers/**").hasAnyRole("ADMIN", "GOV_OFFICER")
                        .requestMatchers("/api/court-cases/**").hasAnyRole("ADMIN", "GOV_OFFICER")
                        .requestMatchers("/api/appointments/lawyer/**").hasAnyRole("ADMIN", "GOV_OFFICER")
                        .requestMatchers("/api/appointments/upcoming").hasAnyRole("ADMIN", "GOV_OFFICER")
                        .requestMatchers("/api/appointments/{id}/confirm").hasAnyRole("ADMIN", "GOV_OFFICER")
                        .requestMatchers("/api/appointments/{id}/complete").hasAnyRole("ADMIN", "GOV_OFFICER")
                        .requestMatchers("/api/reschedule-requests/pending").hasAnyRole("ADMIN", "GOV_OFFICER")
                        .requestMatchers("/api/reschedule-requests/{id}/approve").hasAnyRole("ADMIN", "GOV_OFFICER")
                        .requestMatchers("/api/reschedule-requests/{id}/reject").hasAnyRole("ADMIN", "GOV_OFFICER")
                        // Citizen endpoints
                        .requestMatchers("/api/appointments/**").hasRole("CITIZEN")
                        .requestMatchers("/api/court-cases/user/**").hasRole("CITIZEN")
                        .requestMatchers("/api/reschedule-requests/user/**").hasRole("CITIZEN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

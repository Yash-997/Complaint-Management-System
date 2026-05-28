package com.yash.Complaint_Management_System.config;

import com.yash.Complaint_Management_System.repository.UserRepository;
import com.yash.Complaint_Management_System.security.JwtFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    private final UserRepository userRepository;

    public SecurityConfig(
            JwtFilter jwtFilter,
            UserRepository userRepository
    ) {

        this.jwtFilter = jwtFilter;
        this.userRepository = userRepository;
    }

    // LOAD USER FROM DATABASE
    @Bean
    public UserDetailsService userDetailsService() {

        return email -> userRepository.findByEmail(email)

                .map(user -> org.springframework.security.core.userdetails.User

                        .withUsername(user.getEmail())

                        .password(user.getPassword())

                        .authorities(user.getRole())

                        .build())

                .orElseThrow(() ->

                        new UsernameNotFoundException(
                                "User not found with email: " + email
                        ));
    }

    // PASSWORD ENCODER
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // AUTHENTICATION MANAGER
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {

        return config.getAuthenticationManager();
    }

    // SECURITY FILTER CHAIN
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // ENABLE CORS
                .cors(cors -> {})
                // DISABLE CSRF
                .csrf(csrf -> csrf.disable())
                // STATELESS SESSION
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                // AUTHORIZE REQUESTS
                .authorizeHttpRequests(auth -> auth

                        // PUBLIC APIs

                        .requestMatchers(
                                "/users/register",
                                "/users/login"
                        ).permitAll()
                        // ADMIN APIs
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")
                        // ALL OTHER APIs
                        .anyRequest()
                        .authenticated()
                )
                // JWT FILTER
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration =
                new CorsConfiguration();
        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173",
                        "https://complaint-management-system-fronten-self.vercel.app"
                )
        );
        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }
}
package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.configuration.security;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for Spring Security. This class configures the security settings
 * for the application, including authentication, authorization, and session management.
 * It uses JWT-based authentication with stateless sessions.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${app.version.url}")
    private String VERSION_URL;


    @Autowired
    private SecurityFilter securityFilter; // JWT token filter for validating requests

    @Autowired
    private UserDetailsServiceImpl userDetailsService; // Custom implementation of UserDetailsService for authentication


    /**
     * Configures the {@link AuthenticationManager} bean, which handles the authentication
     * process by coordinating with the authentication provider.
     *
     * @param authenticationConfiguration the configuration for the authentication manager
     * @return the configured {@link AuthenticationManager}
     * @throws Exception if there is an issue creating the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures a password encoder bean that uses BCrypt for hashing passwords.
     *
     * @return the configured {@link PasswordEncoder} bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the {@link AuthenticationProvider} that will authenticate users using
     * the custom {@link UserDetailsServiceImpl} and the BCrypt password encoder.
     *
     * @return the configured {@link AuthenticationProvider}
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configures the {@link SecurityFilterChain} to define how requests should be secured.
     * This configuration disables CSRF protection, sets the session management to stateless
     * (as this is a JWT-based authentication system), and specifies the routes that are
     * publicly accessible and those that require authentication.
     *
     * @param http the {@link HttpSecurity} to configure
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if there is an issue configuring the security filter chain
     */
    @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(VERSION_URL+"/auth/login").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                .authenticationProvider(authenticationProvider())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

package org.theatremanagement.security;

import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfig {

    /**
     * Configures Keycloak authentication provider.
     */
    @Bean
    public KeycloakAuthenticationProvider keycloakAuthenticationProvider() {
        KeycloakAuthenticationProvider provider = new KeycloakAuthenticationProvider();
        provider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        return provider;
    }

    /**
     * Defines session authentication strategy.
     */
    @Bean
    public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new org.springframework.security.core.session.SessionRegistryImpl());
    }

    /**
     * Authentication manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Security filter chain for HTTP requests configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())  // Enables CSRF with cookie storage for token
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/public/**").permitAll()  // Public endpoints
                        .requestMatchers("/admin/**").hasRole("admin")  // Restricted to admin role
                        .anyRequest().authenticated()  // All other endpoints require authentication
                );
        return http.build();
    }
}

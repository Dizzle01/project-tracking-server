package com.example.projecttrackingserver.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

/**
 * Configuration class for setting up security filters, authorization rules and exception handling.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final ApiKeyAuthFilter authFilter;
	private final UnauthorizedHandler unauthorizedHandler;
	
    /**
     * Configures the security filter chain.
     *
     * @param http the HttpSecurity object to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(unauthorizedHandler))
                .securityMatcher("/**")
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers(
                        	// User
                        	AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/companies/{companyId}/users"),
                        	AntPathRequestMatcher.antMatcher(HttpMethod.PATCH, "/api/v1/companies/{companyId}/users/{userId}"),
                        	AntPathRequestMatcher.antMatcher(HttpMethod.PATCH, "/api/v1/companies/{companyId}/users/{userId}/assign-role/{roleId}"),
                        	AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/api/v1/companies/{companyId}/users/{userId}"),
                        	// Project
                    		AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v1/companies/{companyId}/projects"),
                    		AntPathRequestMatcher.antMatcher(HttpMethod.PATCH, "/api/v1/companies/{companyId}/projects/{projectId}"),
                    		AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/api/v1/companies/{companyId}/projects/{projectId}"),
                    		AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v1/companies/{companyId}/projects/{projectId}/add-user/{userId}"),
                    		AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/api/v1/companies/{companyId}/projects/{projectId}/remove-user/{userId}"),
                    		// Ticket
                    		AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v1/companies/{companyId}/projects/{projectId}/tickets"),
                    		AntPathRequestMatcher.antMatcher(HttpMethod.PATCH, "/api/v1/companies/{companyId}/projects/{projectId}/tickets/{ticketId}"),
                    		AntPathRequestMatcher.antMatcher(HttpMethod.DELETE, "/api/v1/companies/{companyId}/projects/{projectId}/tickets/{ticketId}")
                		).authenticated()
                        .requestMatchers( 
                        	// Company
                    		AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/companies"),
                    		AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/companies/{companyId}"),
                        	// User
                    		AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/companies/{companyId}/users/{userId}"),
                    		AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/api/v1/companies/{companyId}/users"),
                        	// Project
                    		AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/companies/{companyId}/projects"),
                    		AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/companies/{companyId}/projects/{projectId}"),
                    		AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/companies/{companyId}/projects/{projectId}/users"),
                        	// Ticket
                    		AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/companies/{companyId}/projects/{projectId}/tickets"),
                    		AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/api/v1/companies/{companyId}/projects/{projectId}/tickets/{ticketId}")
                		).permitAll()
                )
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}

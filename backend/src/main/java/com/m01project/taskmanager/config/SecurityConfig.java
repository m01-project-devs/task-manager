package com.m01project.taskmanager.config;

import com.m01project.taskmanager.security.JwtAuthenticationEntryPoint;
import com.m01project.taskmanager.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Security configuration for the Task Manager application.
 *
 * <p>This configuration sets up:</p>
 * <ul>
 *   <li>JWT-based stateless authentication</li>
 *   <li>Role-based access control</li>
 *   <li>Custom authentication entry point for proper HTTP status codes</li>
 *   <li>Public endpoints for authentication</li>
 * </ul>
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CorsConfigurationSource corsConfigurationSource;

    /**
     * Constructor for dependency injection.
     *
     * @param jwtAuthFilter                  filter that validates JWT tokens on each request
     * @param authenticationProvider         provider that handles authentication logic
     * @param jwtAuthenticationEntryPoint    custom entry point that returns 401 for auth failures
     * @param corsConfigurationSource        CORS configuration for cross-origin requests
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter,
                          AuthenticationProvider authenticationProvider,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
                          CorsConfigurationSource corsConfigurationSource) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    /**
     * Configures the security filter chain for HTTP requests.
     *
     * <p><strong>Configuration Details:</strong></p>
     * <ul>
     *   <li><strong>CSRF:</strong> Disabled since we use stateless JWT authentication</li>
     *   <li><strong>Session Management:</strong> Stateless (no server-side sessions)</li>
     *   <li><strong>Public Endpoints:</strong> /api/auth/*, /swagger-ui/*, /v3/api-docs/*</li>
     *   <li><strong>Protected Endpoints:</strong> /api/users/** (ADMIN role required)</li>
     *   <li><strong>Exception Handling:</strong> Returns 401 instead of 403 for auth failures</li>
     *   <li><strong>JWT Filter:</strong> Runs before UsernamePasswordAuthenticationFilter</li>
     * </ul>
     *
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @SuppressWarnings("java:S4507") // CSRF disabled intentionally for stateless JWT API
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF - not needed for stateless JWT authentication
                .csrf(AbstractHttpConfigurer::disable)

                // Enable CORS with custom configuration
                // This allows frontend applications on different domains to access the API
                // Configuration is managed in CorsConfig.java and can be customized via environment variables
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                // Configure stateless session management (no server-side sessions)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Configure authorization rules for different endpoints
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints - no authentication required
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/auth/forgot-password",
                                "/api/auth/reset-password",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Admin-only endpoints
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )

                // Configure custom exception handling to return 401 for authentication failures
                // This is important: by default, Spring Security returns 403 (Forbidden)
                // but 401 (Unauthorized) is more semantically correct for authentication issues
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        // Return 403 Forbidden when authenticated user lacks required role/permission
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.getWriter().write("""
                                    {"error": "Access denied - insufficient permissions"}
                                    """);
                        })
                )

                // Set the authentication provider
                .authenticationProvider(authenticationProvider)

                // Add JWT filter before the standard username/password authentication filter
                // This ensures JWT tokens are validated on every request
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
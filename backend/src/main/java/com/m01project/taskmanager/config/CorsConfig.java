package com.m01project.taskmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * CORS (Cross-Origin Resource Sharing) configuration for the Task Manager application.
 *
 * <p>This configuration is essential for allowing the frontend application to communicate
 * with the backend API when they are hosted on different domains/origins.</p>
 *
 * <p><strong>Why CORS is needed:</strong></p>
 * <ul>
 *   <li>Browsers enforce the Same-Origin Policy for security</li>
 *   <li>When frontend and backend (Google Cloud Run) are on different domains,
 *       browsers block requests unless CORS headers are properly configured</li>
 *   <li>Without CORS, you'll see "Failed to fetch" or "CORS policy" errors</li>
 * </ul>
 *
 * <p><strong>Configuration via Environment Variables:</strong></p>
 * <ul>
 *   <li>Local development: Set in application.properties</li>
 *   <li>Cloud deployment: Set ALLOWED_ORIGINS in Google Cloud Run environment variables</li>
 *   <li>Multiple origins: Separate with commas (e.g., "https://app1.com,https://app2.com")</li>
 * </ul>
 *
 * @see SecurityConfig where this CORS configuration is integrated
 */
@Configuration
public class CorsConfig {

    /**
     * Allowed origins for CORS requests.
     * Can be configured via environment variable: ALLOWED_ORIGINS
     *
     * <p>Examples:</p>
     * <ul>
     *   <li>Local: http://localhost:3000</li>
     *   <li>Production: https://your-app.vercel.app</li>
     *   <li>Multiple: https://app1.com,https://app2.com,http://localhost:3000</li>
     * </ul>
     */
    @Value("${app.cors.allowed-origins:http://localhost:3000}")
    private String allowedOrigins;

    /**
     * Creates and configures the CORS configuration source.
     *
     * <p><strong>Allowed Settings:</strong></p>
     * <ul>
     *   <li><strong>Origins:</strong> Configurable via ALLOWED_ORIGINS env var</li>
     *   <li><strong>Methods:</strong> GET, POST, PUT, DELETE, OPTIONS, PATCH</li>
     *   <li><strong>Headers:</strong> All headers allowed (Authorization, Content-Type, etc.)</li>
     *   <li><strong>Credentials:</strong> Allowed (needed for cookies/auth headers)</li>
     *   <li><strong>Max Age:</strong> 3600 seconds (preflight cache duration)</li>
     * </ul>
     *
     * <p><strong>Security Note:</strong></p>
     * <ul>
     *   <li>Don't use "*" for allowed origins in production when credentials are true</li>
     *   <li>Always specify exact origins for production deployments</li>
     *   <li>Use environment variables to manage origins per environment</li>
     * </ul>
     *
     * @return configured CorsConfigurationSource bean
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Parse allowed origins from comma-separated string
        // Example: "https://app1.com,https://app2.com,http://localhost:3000"
        List<String> origins = Arrays.asList(allowedOrigins.split(","));
        configuration.setAllowedOrigins(origins);

        // Allow common HTTP methods
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        // Allow all headers (including Authorization for JWT)
        configuration.setAllowedHeaders(List.of("*"));

        // Allow credentials (cookies, authorization headers)
        // This is required for JWT authentication with Authorization header
        configuration.setAllowCredentials(true);

        // Cache preflight response for 1 hour (reduces OPTIONS requests)
        configuration.setMaxAge(3600L);

        // Apply CORS configuration to all endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}


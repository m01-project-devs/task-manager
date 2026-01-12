package com.m01project.taskmanager.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom authentication entry point for JWT-based authentication.
 *
 * <p>This component handles authentication failures and unauthorized access attempts
 * by returning HTTP 401 (Unauthorized) status code instead of the default
 * HTTP 403 (Forbidden) that Spring Security would normally return.</p>
 *
 * <p><strong>Why this is needed:</strong></p>
 * <ul>
 *   <li>HTTP 401 indicates "authentication is required and has failed or not been provided"</li>
 *   <li>HTTP 403 indicates "the request is understood but refused" (access forbidden)</li>
 *   <li>For JWT authentication, 401 is more semantically correct for missing or invalid tokens</li>
 * </ul>
 *
 * <p><strong>When this is triggered:</strong></p>
 * <ul>
 *   <li>No JWT token is provided in the Authorization header</li>
 *   <li>An invalid JWT token is provided</li>
 *   <li>An expired JWT token is provided</li>
 *   <li>JWT signature verification fails</li>
 * </ul>
 *
 * @see com.m01project.taskmanager.config.SecurityConfig where this is configured
 * @see JwtAuthenticationFilter which validates JWT tokens
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Handles authentication exceptions by returning HTTP 401 Unauthorized.
     *
     * @param request       the HTTP request that resulted in an authentication exception
     * @param response      the HTTP response to send back to the client
     * @param authException the exception that was thrown during authentication
     * @throws IOException if an I/O error occurs while writing the response
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // Return 401 Unauthorized for authentication failures
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}


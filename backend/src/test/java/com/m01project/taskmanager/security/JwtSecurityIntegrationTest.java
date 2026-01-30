package com.m01project.taskmanager.security;

import com.jayway.jsonpath.JsonPath;
import com.m01project.taskmanager.BaseIntegrationTest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for JWT-based security configuration.
 *
 * <p>This test class verifies that:</p>
 * <ul>
 *   <li>Public endpoints (login) are accessible without authentication</li>
 *   <li>Valid JWT tokens grant access to protected endpoints</li>
 *   <li>Missing JWT tokens result in HTTP 401 (Unauthorized)</li>
 *   <li>Invalid JWT tokens result in HTTP 401 (Unauthorized)</li>
 *   <li>Expired JWT tokens result in HTTP 401 (Unauthorized)</li>
 * </ul>
 *
 * <p><strong>Why 401 instead of 403?</strong></p>
 * <p>HTTP 401 (Unauthorized) indicates authentication is required but failed or wasn't provided.
 * HTTP 403 (Forbidden) indicates the request is understood but refused (access forbidden).
 * For JWT authentication issues, 401 is semantically correct.</p>
 *
 * @see JwtAuthenticationEntryPoint which ensures 401 is returned
 * @see com.m01project.taskmanager.config.SecurityConfig where the entry point is configured
 */
class JwtSecurityIntegrationTest extends BaseIntegrationTest {

    /** Test JWT secret that matches the one in BaseIntegrationTest configuration */
    private static final String TEST_JWT_SECRET =
            "test-jwt-secret-test-jwt-secret-test-jwt-secret";

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        createTestUser();
    }

    /**
     * Verifies that the /api/auth/login endpoint is publicly accessible.
     *
     * <p>Note: This test expects 400 (Bad Request) because no request body is provided.
     * In a real scenario, missing required fields results in validation errors (400),
     * not authentication errors (401).</p>
     */
    @Test
    void shouldAllowAccessToLoginWithoutJwt() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()); // 400 - missing required body
    }

    /**
     * Verifies that a valid JWT token grants access to protected endpoints.
     *
     * <p>Test flow:</p>
     * <ol>
     *   <li>Login with valid credentials to get a JWT token</li>
     *   <li>Use that token to access a protected endpoint (/api/boards)</li>
     *   <li>Verify the request succeeds (200 OK)</li>
     * </ol>
     */
    @Test
    void shouldAllowAccessWithValidJwt() throws Exception {

        String loginPayload = """
                {
                  "email": "test@example.com",
                  "password": "password"
                }
                """;

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();

        // Extract JWT token from login response
        String token = JsonPath.read(
                result.getResponse().getContentAsString(), "$.token"
        );

        // Use the token to access protected endpoint
        mockMvc.perform(get("/api/boards")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    /**
     * Verifies that requests without a JWT token are rejected with 401 Unauthorized.
     *
     * <p>This ensures that protected endpoints cannot be accessed without authentication.
     * The custom JwtAuthenticationEntryPoint ensures we get 401 (Unauthorized)
     * instead of 403 (Forbidden).</p>
     */
    @Test
    void shouldRejectRequestWithoutJwt() throws Exception {
        mockMvc.perform(get("/api/boards"))
                .andExpect(status().isUnauthorized()); // 401 - missing authentication
    }

    /**
     * Verifies that requests with an invalid JWT token are rejected with 401 Unauthorized.
     *
     * <p>An invalid token could have:</p>
     * <ul>
     *   <li>Incorrect format</li>
     *   <li>Invalid signature</li>
     *   <li>Tampered payload</li>
     * </ul>
     */
    @Test
    void shouldRejectInvalidJwt() throws Exception {
        mockMvc.perform(get("/api/boards")
                        .header("Authorization", "Bearer invalid.token"))
                .andExpect(status().isUnauthorized()); // 401 - invalid authentication
    }

    /**
     * Verifies that requests with an expired JWT token are rejected with 401 Unauthorized.
     *
     * <p>This test creates a token that:</p>
     * <ul>
     *   <li>Was issued 1 hour ago</li>
     *   <li>Expired 30 minutes ago</li>
     * </ul>
     *
     * <p>The JwtAuthenticationFilter should detect the expired token and reject the request.</p>
     */
    @Test
    void shouldRejectExpiredJwt() throws Exception {

        // Create a JWT token that expired 30 minutes ago
        String expiredToken = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(Date.from(Instant.now().minusSeconds(3600)))
                .setExpiration(Date.from(Instant.now().minusSeconds(1800)))
                .signWith(
                        Keys.hmacShaKeyFor(
                                TEST_JWT_SECRET.getBytes(StandardCharsets.UTF_8)
                        ),
                        SignatureAlgorithm.HS256
                )
                .compact();

        mockMvc.perform(get("/api/boards")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized()); // 401 - expired authentication
    }
}

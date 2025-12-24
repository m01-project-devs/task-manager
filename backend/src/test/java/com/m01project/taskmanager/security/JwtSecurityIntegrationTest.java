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

class JwtSecurityIntegrationTest extends BaseIntegrationTest {

    private static final String TEST_JWT_SECRET =
            "test-jwt-secret-test-jwt-secret-test-jwt-secret";

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        createTestUser();
    }

    @Test
    void shouldAllowAccessToLoginWithoutJwt() throws Exception {
        mockMvc.perform(post("/api/auth/login"))
                .andExpect(status().isUnauthorized());
    }

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

        String token = JsonPath.read(
                result.getResponse().getContentAsString(), "$.token"
        );

        mockMvc.perform(get("/api/tasks")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectRequestWithoutJwt() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectInvalidJwt() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .header("Authorization", "Bearer invalid.token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectExpiredJwt() throws Exception {

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

        mockMvc.perform(get("/api/tasks")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized());
    }
}
